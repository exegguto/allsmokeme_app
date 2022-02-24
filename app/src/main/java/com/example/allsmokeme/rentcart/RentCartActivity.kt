package com.example.allsmokeme.rentcart

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.Toolbar
import com.example.allsmokeme.BaseActivity
import com.example.allsmokeme.OnRentCartFragmentDataListener
import com.example.allsmokeme.R
import com.example.allsmokeme.rentlogistic.RentAddressModel
import com.example.allsmokeme.rentlogistic.RentLogisticActivity
import java.util.*


class RentCartActivity : BaseActivity(), OnRentCartFragmentDataListener {
    override fun getViewId(): Int { return R.layout.activity_rentcart}
    private var toolbar: Toolbar? = null
    private lateinit var rentOrder: RentCartModel
    var logistic = arrayOf("Доставка", "Самовывоз")

    // делаем переменные даты/времени полями, т.к. в реальных
    // приложениях они чаще всего используются и в других местах.
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0
    private var time = ""

    override fun onCreateView() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх

        val hookah: TextView = findViewById(R.id.hookah)
        val hookahPrice: TextView = findViewById(R.id.hookahPrice)
        val comments: TextView = findViewById(R.id.comments)
        hookah.text = rentOrder.hookah?.tipe
        hookahPrice.text = rentOrder.hookah?.price_empty.toString()
        comments.text = rentOrder.comments

        val spinner = findViewById<View>(R.id.logistic) as Spinner
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, logistic)
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Применяем адаптер к элементу spinner
        spinner.adapter = adapter
        spinner.setSelection(0)

        val samovivoz = findViewById<TextView>(R.id.samovivoz)
        val logisticAddress = findViewById<TextView>(R.id.logisticAddress)
        val samovivoz1 = findViewById<TableRow>(R.id.samovivoz1)
        val logisticAddress1 = findViewById<TableRow>(R.id.logisticAddress1)

        val itemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Получаем выбранный объект
                val item = parent.getItemAtPosition(position) as String

                when (position) {
                    0 -> {
                        samovivoz1.visibility = View.GONE
                        logisticAddress1.visibility = View.VISIBLE
                        logisticAddress.text = "Выбрать адрес"
                    }
                    1 -> {
                        samovivoz1.visibility = View.VISIBLE
                        logisticAddress1.visibility = View.GONE
                        samovivoz.text = "Для уточнения адреса самовывоза /n с Вами свяжется оператор"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = itemSelectedListener

        logisticAddress.setOnClickListener {
            // создание объекта Intent для запуска RentLogisticActivity
            val intent = Intent(this, RentLogisticActivity::class.java)
            // передача объекта с ключом "rentOrder" и значением rentOrder
            intent.putExtra("rentOrder", rentOrder)
            // запуск RentLogisticActivity
            startActivityForResult(intent, 3)
        }

        val logisticTime =findViewById<TextView>(R.id.logisticTime)

        logisticTime.setOnClickListener {
            // получаем текущую дату
            val cal = Calendar.getInstance()
            mYear = cal[Calendar.YEAR]
            mMonth = cal[Calendar.MONTH]
            mDay = cal[Calendar.DAY_OF_MONTH]
            mHour = cal[Calendar.HOUR_OF_DAY]
            mMinute = cal[Calendar.MINUTE]

            // инициализируем диалог выбора даты текущими значениями
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // инициализируем диалог выбора времени текущими значениями
                    val timePickerDialog = TimePickerDialog(
                        this,
                        { view1, hourOfDay, minute ->
                            time = " $hourOfDay:$minute"
                            logisticTime.text = dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year + time

                        }, mHour, mMinute, true
                    )
                    timePickerDialog.show()
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()

        }


        val nextStep: Button = findViewById(R.id.buyButton)
    }

    override fun getMass(): RentCartModel {
        val arguments = intent.extras
        rentOrder = arguments?.getParcelable("rentOrder")!!
        return rentOrder
    }

/*    fun onClickMix() {
        val intent = Intent()
*//*        intent.putExtra("mixResult", mix)
        intent.putExtra("priseMix", priseMix)*//*
        setResult(RESULT_OK, intent)
        finish()
    }*/

    fun mixEnd(view: View) { //отмена добавления микса
        setResult(RESULT_CANCELED, null)
        finish()}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                val mixTemp = data?.getParcelableExtra<RentAddressModel>("rentAddressModel")

                val logisticAddress = findViewById<TextView>(R.id.logisticAddress)
                logisticAddress.text = mixTemp?.Sity + ", ул. " + mixTemp?.Street + ", д. " +  mixTemp?.House + ", кв. " + mixTemp?.Apartment
            } else {

            }
        }
    }//возвращаем значение при закрытой активити

}