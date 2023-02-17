package com.example.allsmokeme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.allsmokeme.cart.CartActivity
import com.example.allsmokeme.cart.CartHelper.cartItems
import com.example.allsmokeme.rentcart.RentCartActivity
import com.example.allsmokeme.rentcart.RentCartModel
import com.example.allsmokeme.renthookah.RentHookahModel
import com.example.allsmokeme.rentmix.MixActivity
import com.example.allsmokeme.rentmix.MixRentFragment
import com.example.allsmokeme.rentmix.RentMixModel
import com.example.allsmokeme.rentmix.SettingModel
import com.example.allsmokeme.rentset.RentSetEndModel
import com.example.allsmokeme.user.UserActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


open class MainActivity : AppCompatActivity(), OnHomeFragmentDataListener {

    private var bottom_nav_menu: BottomNavigationView? = null
    private var toolbar: Toolbar? = null
    var toolbarTextView: TextView? = null
    private var textCartItemCount: TextView? = null
    private var toast: Toast? = null
    var menuItem: MenuItem? =  null
    private lateinit var rentOrder: RentCartModel //выбранный тип кальяна
    private var listMix: ArrayList<RentMixModel?>? = ArrayList()
    private var rentSetEndModel: ArrayList<RentSetEndModel?> = ArrayList()
    lateinit var settingModel: SettingModel
    var button: Button? = null
    private var mixSum = 0 //Суммарная стоимость миксов
    private var mixSize = 0
    private var holder: Int = 0
    private var edittext1: EditText? = null
    private var SIGN_IN_REQUEST_CODE: Int = 0
    private val db = FirebaseFirestore.getInstance()    //работа с базой данных
    private var number = "tel:+79833198853"
    private var changePositionList = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.help_call, menu)
        menuItem = menu?.findItem(R.id.menu_cart)
        val actionView = menuItem?.actionView
        textCartItemCount = actionView?.findViewById(R.id.cart_badge)
        setupBadge()

//        val myFirmFragment = supportFragmentManager.findFragmentByTag("FirmFragment")
//        if(myFirmFragment != null && myFirmFragment.isVisible){
//            menuItem?.isVisible = true
//
//        }
        when(changePositionList){
            1 -> {
                menuItem?.isVisible = true
                val myProductFragment = supportFragmentManager.findFragmentByTag("ProductFragment")
                val myTobaccoFragment = supportFragmentManager.findFragmentByTag("TobaccoFragment")
                if ((myProductFragment != null && myProductFragment.isVisible)
                    || (myTobaccoFragment != null && myTobaccoFragment.isVisible)
                ) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
                } else {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
            4 -> {
                menuItem?.isVisible = false
                val myMessagesFragment =
                    supportFragmentManager.findFragmentByTag("MessagesFragment")
                if (myMessagesFragment != null && myMessagesFragment.isVisible) {
                    toolbarTextView = findViewById(R.id.toolBarText)
                    toolbarTextView?.visibility = VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
                } else {
                    toolbarTextView = findViewById(R.id.toolBarText)
                    toolbarTextView?.visibility = GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }else -> {
                menuItem?.isVisible = false
                toolbarTextView = findViewById(R.id.toolBarText)
                toolbarTextView?.visibility = GONE
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
        actionView?.setOnClickListener { v: View? -> onOptionsItemSelected(menuItem!!) }
//        return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return   when (item.itemId) {
            R.id.menu_help -> {
                showSearch(); true
            }
            R.id.menu_call -> {
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(number)))
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_cart -> {
                if (!cartItems.isEmpty()) {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                } else {
                    toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
                    toast?.setText(String.format(getString(R.string.cart_dont_message)))
                    toast?.show()
                    false
                }
            }
            R.id.navigation_dashboard_button -> {
                startActivity(Intent(this, UserActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //отображает сообщение над меню боттом
    private fun showSearch() {
        val snack: Snackbar = Snackbar.make(
            findViewById(R.id.bottom_nav_menu),
            "Go to Search",
            Snackbar.LENGTH_INDEFINITE
        )
        snack.setAction("Ok") { v: View? -> snack.dismiss() }
        val params =
            snack.view.layoutParams as CoordinatorLayout.LayoutParams
        params.anchorId = R.id.bottom_nav_menu
        params.anchorGravity = Gravity.TOP
        params.gravity = Gravity.TOP
        snack.view.layoutParams = params
        snack.show()
    }


    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
                result -> onSignInResult(result)
        }

    open fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            val docRef = db.collection("users").document(user!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (!document.exists()) {
                        val userNew: MutableMap<String, String?> = HashMap()
                        userNew["userName"] = user.displayName
                        userNew["userPhone"] = user.phoneNumber

                        db.collection("users").document(user.uid).set(userNew)
                    } else {

                    }
                }
                .addOnFailureListener { exception ->
                }

            Toast.makeText(
                this,
                "Successfully signed in. Welcome!",
                Toast.LENGTH_LONG
            ).show()

            oneStep()
        } else {
            Toast.makeText(
                this,
                "We couldn't sign you in. Please try again later.",
                Toast.LENGTH_LONG
            ).show()
            // Close the app
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null) {
            // Start sign in/sign up activity
            signInLauncher.launch(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(listOf(AuthUI.IdpConfig.PhoneBuilder().build()))
                    .build()
//                SIGN_IN_REQUEST_CODE
            )
        } else {
            oneStep()
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(
                this,
                "Welcome " + FirebaseAuth.getInstance()
                    .currentUser!!
                    .displayName,
                Toast.LENGTH_LONG
            )
                .show()
        }

    }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                v.clearFocus()
                val imm: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }
        return super.dispatchTouchEvent(event)
    }//скрываем клавиатуру

    fun oneStep(){
        settingsFirebase()

        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val adapter1 = AppFragmentPageAdapter(supportFragmentManager)
        viewPager.adapter = adapter1
        viewPager.offscreenPageLimit = adapter1.count - 1
        bottom_nav_menu = findViewById(R.id.bottom_nav_menu)
        val listener = BottomNavItemSelectedListener(viewPager)
        bottom_nav_menu!!.setOnNavigationItemSelectedListener(listener)

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                changePositionList = position
                return when (position) {
                    1 -> {
                        menuItem?.isVisible = true
                        val myProductFragment =
                            supportFragmentManager.findFragmentByTag("ProductFragment")
                        val myTobaccoFragment =
                            supportFragmentManager.findFragmentByTag("TobaccoFragment")
                        if ((myProductFragment != null && myProductFragment.isVisible) ||
                            (myTobaccoFragment != null && myTobaccoFragment.isVisible)
                        ) {
                            supportActionBar!!.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
                        } else {
                            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                        }
                        toolbarTextView = findViewById(R.id.toolBarText)
                        toolbarTextView?.visibility = GONE
                    }
                    4 -> {
                        menuItem?.isVisible = false
                        val myMessagesFragment =
                            supportFragmentManager.findFragmentByTag("MessagesFragment")
                        if (myMessagesFragment != null && myMessagesFragment.isVisible) {
                            supportActionBar!!.setDisplayHomeAsUpEnabled(true) //установка кнопки наверх
                            toolbarTextView = findViewById(R.id.toolBarText)
                            toolbarTextView?.visibility = VISIBLE
                        } else {
                            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                        }

                    }
                    else -> {
                        menuItem?.isVisible = false
                        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                        toolbarTextView = findViewById(R.id.toolBarText)
                        toolbarTextView?.visibility = GONE
                    }
                }
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

//        edittext1 = findViewById(R.id.editText1) //

    }

    private fun setupBadge() {
        if (textCartItemCount != null) {
            if (cartItems.isEmpty()) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = Math.min(cartItems.size, 99).toString()
                if (textCartItemCount!!.visibility != VISIBLE) {
                    textCartItemCount!!.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun addVkusOnClick(view: View?) {
        holder = view?.tag as Int //на какую строчку нажали
        startActivityForResult(Intent(this, MixActivity::class.java), 1)
    } // Запускаем Активити с выбором микса

    fun rentCartOnClick(view: View?) {
        if(rentOrder.hookah == null) {
            toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
            toast?.setText("Нужно выбрать тип кальяна")
            toast?.show()
            return
        }
        val comments: TextView = findViewById(R.id.editText1)!!
        rentOrder.comments = comments.text.toString()
        // создание объекта Intent для запуска RentCartActivity
        val intent = Intent(this, RentCartActivity::class.java)
        // передача объекта с ключом "rentOrder" и значением rentOrder
        intent.putExtra("rentOrder", rentOrder)
        // запуск RentCartActivity
        startActivityForResult(intent, 2)
    }// Запускаем Активити с корзиной аренды

    override fun reCalcRent1Set(listMixTemp: ArrayList<RentSetEndModel?>){
        var num2 = 0
        if(rentOrder.rent1SetEndModel.size > 0) rentOrder.rent1SetEndModel.removeAll(rentOrder.rent1SetEndModel)
        for(num1 in 0 until listMixTemp.size)
            if (listMixTemp[num1]?.quantity == 1){
                rentOrder.rent1SetEndModel.add(RentSetEndModel())
                rentOrder.rent1SetEndModel[num2++] = listMixTemp[num1]
            }
        updateSumRentOrder()
    }//получаем первую часть массива с допами

    override fun reCalcRent2Set(listMixTemp: ArrayList<RentSetEndModel?>){
        var num2 = 0
        if(rentOrder.rent2SetEndModel.size > 0) rentOrder.rent2SetEndModel.removeAll(rentOrder.rent2SetEndModel)
        for(num1 in 0 until listMixTemp.size)
            if (listMixTemp[num1]?.quantity!! > 0){
                rentOrder.rent2SetEndModel.add(RentSetEndModel())
                rentOrder.rent2SetEndModel[num2++] = listMixTemp[num1]
            }
        updateSumRentOrder()
    }//получаем вторую часть массива с допами

    override fun delMixOnClick(listMixTemp: ArrayList<RentMixModel?>, num: Int) {
        listMix = listMixTemp
        if(num >= 0){
            val list = listMix!![num]
            if (list?.priseMix1 == 0 && list.priseMix2 != 0) {
                list.mix1 = list.mix2
                list.priseMix1 = list.priseMix2
                list.mix2 = list.mix3
                list.priseMix2 = list.priseMix3
                list.mix3 = getString(R.string.chooseTaste)
                list.priseMix3 = 0
            }
            if (list?.priseMix1 == 0 && list.priseMix2 == 0 && list.priseMix3 != 0) {
                list.mix1 = list.mix3
                list.priseMix1 = list.priseMix3
                list.mix3 = getString(R.string.chooseTaste)
                list.priseMix3 = 0
            }
            if (list?.priseMix1 != 0 && list?.priseMix2 == 0 && list.priseMix3 != 0) {
                list.mix2 = list.mix3
                list.priseMix2 = list.priseMix3
                list.mix3 = getString(R.string.chooseTaste)
                list.priseMix3 = 0
            }
            listMix!![num] = list
        }
        reCalcRentOrderMix()
    }//удаление миксов

    override fun fragmentListMix(listMixTemp: ArrayList<RentMixModel?>){
        listMix = listMixTemp
    }//получаем из фрагмента массив

    override fun rentListMix(listMixTemp: ArrayList<RentMixModel?>){
        if(listMixTemp[listMixTemp.size - 1]?.prise1 != settingModel.prise1)
            listMixTemp[listMixTemp.size - 1]?.prise1 = settingModel.prise1
        if(listMixTemp[listMixTemp.size - 1]?.prise2 != settingModel.prise2)
            listMixTemp[listMixTemp.size - 1]?.prise2 = settingModel.prise2
        if(listMixTemp[listMixTemp.size - 1]?.priseScore != settingModel.priseScore)
            listMixTemp[listMixTemp.size - 1]?.priseScore = settingModel.priseScore
        if(listMixTemp[listMixTemp.size - 1]?.priseScorePineaple != settingModel.priseScorePineaple)
            listMixTemp[listMixTemp.size - 1]?.priseScorePineaple = settingModel.priseScorePineaple
        listMix = listMixTemp
        reCalcRentOrderMix()
    }//получаем из фрагмента массив

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
//                val user = FirebaseAuth.getInstance().currentUser
//                val docRef = db.collection("users").document(user!!.uid)
//                docRef.get()
//                    .addOnSuccessListener { document ->
//                        if (!document.exists()) {
//                            val userNew: MutableMap<String, String?> = HashMap()
//                            userNew["userName"] = user.displayName
//                            userNew["userPhone"] = user.phoneNumber
//
//                            db.collection("users").document(user.uid).set(userNew)
//                        } else {
//
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                    }
//
//                Toast.makeText(
//                    this,
//                    "Successfully signed in. Welcome!",
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//
//                oneStep()
            } else {
//                Toast.makeText(
//                    this,
//                    "We couldn't sign you in. Please try again later.",
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//                // Close the app
//                finish()
            }

        if (it.resultCode == 1) {
            val listTemp1 = listMix?.get(holder)
            val mixTemp = it.data?.getStringExtra("mixResult")
            val priseMixTemp = it.data?.getIntExtra("priseMix", 0)

            if (listTemp1?.mix1 == getString(R.string.chooseTaste)) {
                listTemp1.mix1 = mixTemp!!
                listTemp1.priseMix1 = priseMixTemp!!
            } else {
                if (listTemp1?.mix2 == getString(R.string.chooseTaste)) {
                    listTemp1.mix2 = mixTemp!!
                    listTemp1.priseMix2 = priseMixTemp!!
                } else {
                    listTemp1?.mix3 = mixTemp!!
                    listTemp1?.priseMix3 = priseMixTemp!!
                }
            }
            listTemp1?.switch1 = false
            listTemp1?.switch2 = false
            listMix?.set(holder, listTemp1)
            MixRentFragment().listTemp?.clear()
            MixRentFragment().listTemp = listMix

            reCalcRentOrderMix()
        }
    }//возвращаем значение при закрытой активити

    private fun reCalcRentOrderMix(){
        rentOrder.mix = listMix!!

        mixSum = 0
        mixSize = rentOrder.mix.size
        for(num in 0 until  mixSize){
            if(rentOrder.mix[num]?.switch1!!){
                mixSum += rentOrder.mix[num]?.prise1!!
            }else
                if(rentOrder.mix[num]?.switch2!!){
                    mixSum += rentOrder.mix[num]?.prise2!!
                }else{
                    var priseMix: Int = -1
                    if (priseMix < rentOrder.mix[num]?.priseMix1!!) priseMix = rentOrder.mix[num]?.priseMix1!!
                    if (priseMix < rentOrder.mix[num]?.priseMix2!!) priseMix = rentOrder.mix[num]?.priseMix2!!
                    if (priseMix < rentOrder.mix[num]?.priseMix3!!) priseMix = rentOrder.mix[num]?.priseMix3!!

                    mixSum += priseMix
                }
            if(rentOrder.mix[num]?.score!!){
                mixSum += rentOrder.mix[num]?.priseScore!!
            }else
                if(rentOrder.mix[num]?.scorePineaple!!){
                    mixSum += rentOrder.mix[num]?.priseScorePineaple!!
                }
        }
        updateSumRentOrder()
    }//пересчитываем сумму миксов

/*    override fun addMix(){
        val addMixClick: Button = findViewById(R.id.addMix)!!
        addMixClick.performClick()
    }//Добавить новый мик (зачастую при изменение чаш на фруктах)*/

    override fun onOpenMixRentFragment(rentHookahModel: RentHookahModel?) {
        val linearLayout1: LinearLayout = findViewById(R.id.mixRentFragment)
        linearLayout1.visibility = VISIBLE
        val linearLayout2: RelativeLayout = findViewById(R.id.layout_bottom_bar)
        linearLayout2.visibility = VISIBLE
        rentOrder = RentCartModel()
        if(rentHookahModel != null) {
            rentOrder.hookah = RentHookahModel()
            rentOrder.hookah = rentHookahModel
        }
        else rentOrder.hookah = null
        updateSumRentOrder()
    }//показываем миксы и остальные поля после выбора кальяна

    fun updateSumRentOrder(){
        if(rentOrder.hookah == null) return
        var setSum1 = 0
        val setSize1 = rentOrder.rent1SetEndModel.size
        for(num in 0 until setSize1) setSum1 += rentOrder.rent1SetEndModel[num]!!.quantity * rentOrder.rent1SetEndModel[num]!!.prise.toInt()

        var setSum2 = 0
        val setSize2 = rentOrder.rent2SetEndModel.size
        for(num in 0 until setSize2) setSum2 += rentOrder.rent2SetEndModel[num]!!.quantity * rentOrder.rent2SetEndModel[num]!!.prise.toInt()

        rentOrder.sumAll = rentOrder.hookah!!.price_empty!! + mixSum +setSum1 + setSum2
        if(mixSize > 0 )rentOrder.sumOne = rentOrder.sumAll /(mixSize)
        else rentOrder.sumOne = 0

        val text1: TextView = findViewById(R.id.textView1)
        val text2: TextView = findViewById(R.id.textView2)

        text1.text =  getString(R.string.rentSum) + rentOrder.sumAll
        text2.text = getString(R.string.rentSumAll) + rentOrder.sumOne

    }//пересчитать корзину


    private fun settingsFirebase(){

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.CACHE

        db.collection("rent_set").document("3")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        settingModel = document.toObject<SettingModel>(
                            SettingModel::class.java
                        )!!
                        settingModel.prise1 = (document["prise1"] as Long).toInt()
                        settingModel.prise2 = (document["prise2"] as Long).toInt()
                        settingModel.priseScore = (document["priseScore"] as Long).toInt()
                        settingModel.priseScorePineaple = (document["priseScorePineaple"] as Long).toInt()
                    }
                }
            }.addOnFailureListener { //exception -> textView.setText("Error getting documents: ")
            }

    }

    override fun getSettingsFirebase(): SettingModel { return settingModel }

    override fun onBackPressed() {
        when(changePositionList){
            1 -> {
                val fragment =
                    this.supportFragmentManager.findFragmentById(R.id.container)
                if (!(fragment as IOnBackPressed).onBackPressed()) {
                    super.onBackPressed()
                }
            }
            4 -> {
                val fragment =
                    this.supportFragmentManager.findFragmentById(R.id.container_chats)
                if (!(fragment as IOnBackPressed).onBackPressed()) {
                    super.onBackPressed()
                }
            }
        }
        return
    }



}