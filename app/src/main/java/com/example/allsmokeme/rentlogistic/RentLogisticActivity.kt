package com.example.allsmokeme.rentlogistic

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseActivity
import com.example.allsmokeme.OnRentLogisticDataListener
import com.example.allsmokeme.R
import com.example.allsmokeme.chat.MessagesFragment
import com.example.allsmokeme.rentcart.RentCartModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList


class RentLogisticActivity : BaseActivity(), OnRentLogisticDataListener, PlacesAutoCompleteAdapter.ClickListener, AddressCompleteAdapter.OnItemClickListener {
    override fun getViewId(): Int { return R.layout.activity_rentlogistic}
    private var toolbar: Toolbar? = null
    var dlg: DialogFragment? = null
    private lateinit var rentOrder: RentCartModel
    var textIntercom = arrayOf("Я выйду", "Работает", "Нет домофона")

    var token: AutocompleteSessionToken? = null
    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var seach: Boolean = true

    private val addressList: MutableList<RentAddressModel> = ArrayList()
    private var recyclerViewAddress: RecyclerView? = null

    private val user = FirebaseAuth.getInstance().currentUser
    private var dbSnapshot: ListenerRegistration? = null
    var userUid = ""
    val db = FirebaseFirestore.getInstance()
    var latitude = ""
    var longitude = ""

    override fun onCreateView() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх

//        val arguments = intent.extras
//        rentOrder = arguments?.getParcelable("rentOrder")!!

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rentlogistic)
        onCreateView()

        //загрузка списка адресов из базы
        userUid = user!!.uid

        val dbtemp = db.collection("users")
            .document(userUid)
            .collection("address")
            .whereEqualTo("Visibility", true)

        dbSnapshot = dbtemp.addSnapshotListener { snapshots, e ->

            if (e != null) {
//                    Log.w(TAG, "listen:error", e)
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        val addressModel = dc.document.toObject<RentAddressModel>(
                            RentAddressModel::class.java
                        )
                        addressModel.addressID = dc.document.id
                        addressList.add(addressModel)
                    }
                    DocumentChange.Type.MODIFIED -> {
                    }
                    DocumentChange.Type.REMOVED -> {
                        var goodDelete = true
                        var num = 0
                        while (goodDelete && num < addressList.size) {
                            if(addressList[num].addressID == dc.document.id) {
                                addressList.removeAt(num)
                                goodDelete = false
//                                    recyclerView!!.adapter?.notifyItemRemoved(num)
                            }else
                                num++
                        }
                    }
                }
            }

            if(addressList.size == 0) {}

            val addressCompleteAdapter = AddressCompleteAdapter(
                this,
                addressList,
                "RentLogisticActivity"
            ).also {
                it.setOnItemClickListener(this)
            }
            recyclerViewAddress = this.findViewById(R.id.adress_recycler_view)
            recyclerViewAddress!!.layoutManager = LinearLayoutManager(this)
            recyclerViewAddress!!.adapter = addressCompleteAdapter

        }


        //загрузка гугл адресов
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.map_api_key))
        }

        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        dlg = RentMapFragment()

        recyclerView = findViewById(R.id.places_recycler_view)

        val textSity =  findViewById<TextView>(R.id.textSity)
        val textStreet =  findViewById<EditText>(R.id.textStreet)
        val textHouse =  findViewById<EditText>(R.id.textHouse)

        textStreet.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                textStreet.addTextChangedListener(filterTextWatcher)
                // Создаем новый токен для сеанса автозаполнения. Передайте это FindAutocompletePredictionsRequest,
                // и еще раз, когда пользователь делает выбор (например, при вызове fetchPlace ()).
                token = AutocompleteSessionToken.newInstance()

                mAutoCompleteAdapter = PlacesAutoCompleteAdapter(this, token!!)
                recyclerView?.layoutManager = LinearLayoutManager(this)
                mAutoCompleteAdapter!!.setClickListener(this)
                recyclerView?.adapter = mAutoCompleteAdapter
                mAutoCompleteAdapter!!.notifyDataSetChanged()
            }
        }

        val spinner = findViewById<View>(R.id.textIntercom) as Spinner
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, textIntercom)
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Применяем адаптер к элементу spinner
        spinner.adapter = adapter
        spinner.prompt = "Я выйду"
        // выделяем элемент
        spinner.setSelection(0)
        val itemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Получаем выбранный объект
                val item = parent.getItemAtPosition(position) as String
                spinner.prompt = item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = itemSelectedListener

        //Добавление адреса доставки в базу
        val buyButton = findViewById<Button>(R.id.buyButton)

        buyButton.setOnClickListener {
            if(textSity.text != "" && textStreet.text.toString() != "" && textHouse.text.toString() != "") {
            //здесь добавляем адресс в базу
                val textEntrance =  findViewById<EditText>(R.id.textEntrance)
                val textFloor =  findViewById<EditText>(R.id.textFloor)
                val textApartment =  findViewById<EditText>(R.id.textApartment)
//                val price =  findViewById<EditText>(R.id.price)

                val hashAddress = hashMapOf(
                    "Sity" to textSity.text,
                    "Street" to textStreet.text.toString(),
                    "House" to textHouse.text.toString(),
                    "Entrance" to textEntrance.text.toString(),
                    "Floor" to textFloor.text.toString(),
                    "Apartment" to textApartment.text.toString(),
                    "Intercom" to spinner.prompt,
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "Visibility" to true,
                    "price" to 0
                )
                val newAddress = db.collection("users")
                    .document(userUid)
                    .collection("address")
                    .document()

                newAddress.set(hashAddress)
            }else{
                val toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
                toast.setText(String.format(
                    "Заполните обязательные поля"
                ))
                toast.show()
            }
        }
    }

    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.length > 3 && seach) {

                mAutoCompleteAdapter!!.filter.filter(s.toString())
                if (recyclerView!!.visibility == View.GONE) {
                    recyclerView!!.visibility = View.VISIBLE
                }
            } else {
                if (recyclerView!!.visibility == View.VISIBLE) {
                    recyclerView!!.visibility = View.GONE
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    override fun click(place: Place?) {
        findViewById<EditText>(R.id.textStreet).setText(place?.name.toString().replace("улица", ""))
        findViewById<EditText>(R.id.textHouse).isFocusableInTouchMode = true
        recyclerView!!.visibility = View.GONE
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.ic_map ->{
                seach = false
                dlg?.show(supportFragmentManager, "RentLogisticActivity")}
            else -> {
            }
        }
    }


    fun mixEnd(view: View) { //отмена добавления микса

    }

    override fun onDialogClickListener(latLng: LatLng) {
        val addresses: List<Address> = Geocoder(
            this@RentLogisticActivity,
            Locale.getDefault()
        ).getFromLocation(
            latLng.latitude, latLng.longitude,
            1
        )
        latitude = latLng.latitude.toString()
        longitude = latLng.longitude.toString()

        findViewById<TextView>(R.id.textSity).text = addresses[0].locality.toString()
        findViewById<TextView>(R.id.textStreet).text =
            addresses[0].thoroughfare.toString().removePrefix(
                "улица "
            )
        findViewById<TextView>(R.id.textHouse).text =
            addresses[0].subThoroughfare.toString()
        seach = true
    }

    override fun onItemClick(rentAddressModel: RentAddressModel) {
        dbSnapshot?.remove()

        intent.putExtra("rentAddressModel", rentAddressModel)
        setResult(RESULT_OK, intent)
        finish()

/*
        val messagesFragment = MessagesFragment()
        val bundle: Bundle? = arguments
        bundle?.putString("chatID", rentAddressModel!!.chatID)
        bundle?.putString("userUid", userUid)
        bundle?.putString("nameChat", rentAddressModel!!.nameChat)

        messagesFragment.arguments = bundle

        dbSnapshot?.remove()
*/

//        parentFragmentManager.beginTransaction()
//            .replace(R.id.container_chats, messagesFragment,"MessagesFragment")
////            .addToBackStack("MessagesFragment")
//            .commit()
    }

}