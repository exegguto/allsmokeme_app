package com.example.allsmokeme.rentlogistic

import android.content.Context
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class PlacesAutoCompleteAdapter(private val mContext: Context, private val token: AutocompleteSessionToken) :
    RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(), Filterable {
    private var mResultList: ArrayList<PlaceAutocomplete>? = ArrayList()
    private val placesClient: PlacesClient
    private var clickListener: ClickListener? = null
    fun setClickListener(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun click(place: Place?)
    }

    //Возвращает фильтр для текущего набора результатов автозаполнения.

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                // Пропустить запрос автозаполнения, если не задано никаких ограничений.
                if (constraint != null) {
                    // Запрашиваем у API автозаполнения строку поиска (ограничение).
                    mResultList = getPredictions(constraint)
                    if (mResultList != null) {
                        // API успешно вернул результаты.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results != null && results.count > 0) {
                    // API вернул хотя бы один результат, обновите данные.
                    notifyDataSetChanged()
                } else {
                    // API не вернул никаких результатов, аннулирует набор данных.
                    //notifyDataSetInvalidated();
                }
            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutocomplete> {
        val resultList = ArrayList<PlaceAutocomplete>()

        // Создайте объект RectangularBounds.
        val bounds = RectangularBounds.newInstance(
            LatLng(54.740878, 82.460228),
            LatLng(55.367662, 83.232197)
        )

        // Использование построителя для создания FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder() // Вызвать либо setLocationBias (), либо setLocationRestriction ().
                .setLocationBias(bounds)
                .setCountry("RU")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()
        val autocompletePredictions = placesClient.findAutocompletePredictions(request)

        // Этот метод должен был быть вызван из основного потока пользовательского интерфейса. Заблокировать и ждать не более
        // 60 секунд для результата из API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }
        return if (autocompletePredictions.isSuccessful) {
            val findAutocompletePredictionsResponse = autocompletePredictions.result
            if (findAutocompletePredictionsResponse != null) for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                resultList.add(
                    PlaceAutocomplete(
                        prediction.placeId,
                        prediction.getPrimaryText(StyleSpan(Typeface.NORMAL)).toString()
                    )
                )
            }
            resultList
        } else {
            resultList
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PredictionHolder {
        val layoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView: View =
            layoutInflater.inflate(R.layout.item_place, viewGroup, false)
        return PredictionHolder(convertView)
    }

    override fun onBindViewHolder(mPredictionHolder: PredictionHolder, i: Int) {
        mPredictionHolder.area.text = mResultList!![i].area.toString().replace("улица", "")
    }

    override fun getItemCount(): Int {
        return mResultList!!.size
    }

    fun getItem(position: Int): PlaceAutocomplete {
        return mResultList!![position]
    }

    inner class PredictionHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val area: TextView
        override fun onClick(v: View) {
            if(v.id == R.id.textTable) {
                val item = mResultList!![adapterPosition]
                val placeId = item.placeId.toString()
                val placeFields = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
                )
                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient.fetchPlace(request).addOnSuccessListener { response ->
                    val place = response.place
                    clickListener!!.click(place)
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Toast.makeText(mContext, exception.message + "", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        init {
            area = itemView.findViewById(R.id.textSitySeach)
            itemView.setOnClickListener(this)
        }
    }

    /**
     * Держатель результатов API автозаполнения геоданных мест.
     */
    inner class PlaceAutocomplete internal constructor(
        var placeId: CharSequence,
        var area: CharSequence
    )/* {
        override fun toString(): String {
            return area.toString()
        }
    }
*/
    init {
        placesClient = Places.createClient(mContext)
    }
}