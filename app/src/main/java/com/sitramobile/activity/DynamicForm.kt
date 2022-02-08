package com.sitramobile.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sitramobile.R
import com.sitramobile.adapter.DynamicFormAdapter
import com.sitramobile.api.ApiClient
import com.sitramobile.db.AppDatabase
import com.sitramobile.db.DynamicData
import com.sitramobile.modelRequest.*
import com.sitramobile.modelResponse.*
import com.sitramobile.utils.Coroutines
import com.sitramobile.utils.Helper
import com.sitramobile.utils.Helper.PT_Id
import com.sitramobile.utils.Helper.getCustomerId
import com.sitramobile.utils.Helper.getUserName
import com.sitramobile.utils.MyFunctions
import com.sitramobile.utils.ViewUtils.getKeyName
import com.sitramobile.utils.ViewUtils.getKeyVaule
import com.sitramobile.utils.ViewUtils.isInternetAvailable
import com.sitramobile.utils.ViewUtils.showToast
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.coroutines.delay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DynamicForm : AppCompatActivity() {


    lateinit var mToolbar: Toolbar
    lateinit var recycler_view: RecyclerView
    lateinit var listAdapter : DynamicFormAdapter
    lateinit var progressBar: ProgressBar
    lateinit var button_layout: LinearLayout
    lateinit var cancel: Button
    lateinit var save: Button
    var screen_no:String = "";
    // var listFormData:List<FieldResponse> =  ArrayList<FieldResponse>()
    // This is Dynamic activity and call api

    var listFormData : ArrayList<FieldResponse> = ArrayList<FieldResponse>()

    var listFormDropdownData : ArrayList<FormlookupResponseItem> = ArrayList<FormlookupResponseItem>()
    var listDropdownData : ArrayList<FormDropdownwithlist> = ArrayList<FormDropdownwithlist>()

    lateinit var  spinner: SearchableSpinner
    lateinit var  mRunnerSpinner: SearchableSpinner
    lateinit var  mSyncDialog: Dialog
    lateinit var myMenu:Menu
    lateinit var db : AppDatabase
    var PT_Run_No:String?=null
    lateinit var runnerFormDesign:LinearLayout
    lateinit var dynamicdataform: LinearLayout

    var FormPTList : ArrayList<FormSublistItem> = ArrayList<FormSublistItem>()

    lateinit var FormPTList2 : JsonArray


    //var mRunnerSpinnerAdapter:ArrayAdapter<String>?=null
    //var PT_KEY_SubitemName:String?=null
    //var PT_KEY_SubitemValue:String?=null

    public var PrimaryKey_Name:String ?=null
    public var PrimaryKey_value:String ?=null

    var SecondaryKey_Name:String?=null
    var SecondaryKey_value:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dynamic_form)

        PrimaryKey_Name=null
        PrimaryKey_value=null

        SecondaryKey_Name=null
        SecondaryKey_value=null

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        spinner = findViewById<View>(R.id.spinner) as SearchableSpinner
        runnerFormDesign = findViewById<View>(R.id.runnner_list) as LinearLayout
        dynamicdataform= findViewById<View>(R.id.dynamicdataform) as LinearLayout
        mRunnerSpinner = findViewById<View>(R.id.mRunner_spinner) as SearchableSpinner
        val mUsername = findViewById<View>(R.id.user_name) as TextView

        mUsername.setText("Username : "+getUserName())
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val title = intent.getStringExtra("category_name")!!

        if(title!=null && title.length>10) { mToolbar.title = title.substring(0, 10) + "..." }
        else if(title!=null){ mToolbar.title = title }
        TooltipCompat.setTooltipText(mToolbar, title)
//        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
//            mToolbar.tooltipText=title
//        }else */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            TooltipCompat.setTooltipText(mToolbar, title)
//        }

       // mToolbar.title = intent.getStringExtra("category_name")!!

        screen_no = intent.getStringExtra("screen_no")!!

        db = AppDatabase.invoke(this)

        Log.e("category", intent.getStringExtra("category_name").toString())

        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        recycler_view = findViewById<View>(R.id.recycler_view) as RecyclerView
        button_layout = findViewById<View>(R.id.button_layout) as LinearLayout
        cancel = findViewById<View>(R.id.cancel) as Button
        save = findViewById<View>(R.id.save) as Button

        mSyncDialog = Dialog(this@DynamicForm, R.style.CustomDialog)

        /* val layoutManager = GridLayoutManager(this, 2)
         layoutManager.orientation = RecyclerView.VERTICAL
         recycler_view.layoutManager = layoutManager*/

        cancel.setOnClickListener { finish() }

        save.setOnClickListener(View.OnClickListener {
            val paramObject = JsonObject()
            paramObject.addProperty("custid", getCustomerId())
            paramObject.addProperty("ScreenNo", screen_no)
            /*if(PT_Run_No!=null) {
                paramObject.addProperty("PT_Run_No", PT_Run_No)
            }*/


            for (model in listFormData) {
                paramObject.addProperty(model.Field_Name, model.fieldValue)
                if (model.Mandatory == "1" && model.fieldValue.isNullOrEmpty()){//(model.fieldValue==null || model.fieldValue?.isEmpty() == true)) {
                    showDialog()
                    return@OnClickListener
                }
            }

            Log.e("checking", paramObject.toString())

            if(isInternetAvailable()) {

                Log.e("internet","Available")
                callUpdateFieldData(paramObject)

            }else {
                Log.e("internet", "Unavailable")

                Coroutines.io {

                    insertOrUpdate( DynamicData(
                        paramObject["custid"].toString(),
                        paramObject["ScreenNo"].toString(),
                        paramObject.toString()
                    ))
                    val allDynamicData = db.getDynamicDataDao().getAllDynamicData()

                    Coroutines.main {
                        showToast("No Internet Connection so Data Stored in Local Database")
                        for (i in allDynamicData) {
                            Log.e(
                                "data",
                                i.id.toString() + "-" + i.custid + "-" + i.screenNo + "-" + i.data
                            )
                        }
                    }
                }
            }
        })


        if (this.isInternetAvailable()) {
            var i=0;
            var mOfflineData:List<DynamicData> = mutableListOf()
            Coroutines.io {
                mOfflineData = db.getDynamicDataDao().getAllDynamicData()
                Coroutines.main {
                    i = mOfflineData.size
                    if (i == 0) {
                        //callGetFieldData()
                       // getSpinnerItem()
                        getSpinnerItem2()
                    } else {
                        showSyncDialog(mOfflineData)
                    }
                }
            }
        } else {
            showToast("Please Check Internet Connection")

        }

    }





    suspend fun insertOrUpdate(item: DynamicData) {
        //Coroutines.io {
        db.runInTransaction {
            Coroutines.io {
                val id = db.getDynamicDataDao().filterData(item.custid, item.screenNo)
                if (id == null) {
                    db.getDynamicDataDao().insert(item)
                    Log.e("insert", "Successfully")
                } else {

                    db.getDynamicDataDao().update(item)
                    Log.e("update", "Successfully")
                }
            }
        }
        //}
    }



    private fun showDialog() {

        val dialog = Dialog(this@DynamicForm, R.style.CustomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.error_alert)
        val close = dialog.findViewById<ImageView>(R.id.close_alert)
        close.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showSyncDialog(mOfflineData:List<DynamicData>) {

        mSyncDialog = Dialog(this@DynamicForm, R.style.CustomDialog)
        mSyncDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mSyncDialog.setCancelable(true)
        mSyncDialog.setContentView(R.layout.sync_now)
        val SyncData = mSyncDialog.findViewById<Button>(R.id.bnt_sync)

        SyncData.setOnClickListener {
            SyncData.isEnabled=false
            // mSyncDialog.dismiss()
            for (j in mOfflineData) {

                progressBar.visibility = View.VISIBLE
                val gson = Gson()
                Log.e("data",j.data.toString())
                val apiInterface = ApiClient.invoke()//.create(ApiInterface::class.java)
                val mydata=gson.fromJson(j.data, JsonObject::class.java)
                val call2 = apiInterface.updateFieldData(mydata)
                call2!!.enqueue(object : Callback<Boolean?> {
                    override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                        progressBar.visibility = View.GONE
                        assert(response.body() != null)
                        if (response.isSuccessful) {
                            if (response.body()!=null && response.body()==true) {
                                Toast.makeText(
                                    this@DynamicForm,
                                    "Form updated successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                Coroutines.io {
                                    db.getDynamicDataDao().delete(j)
                                    Log.e("delected","successfully")
                                }

                            } else Toast.makeText(
                                this@DynamicForm,
                                "Form update Failed",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@DynamicForm,
                                response.code().toString() + " " + response.body().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        Log.e("Failure", "Fail")
                    }
                })
            }

            Coroutines.main {
                delay(3000)
                mSyncDialog.dismiss()
                finish()
            }
        }
        mSyncDialog.show()

    }


    private fun getSpinnerItem2() {

        progressBar.visibility = View.VISIBLE
        val call2 = ApiClient.invoke().getItemListData2(ObservationDataList(getCustomerId(), intent.getStringExtra("screen_no")!!))
        call2.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                progressBar.visibility = View.GONE
                ////assert(response.body() != null)
                if(response.isSuccessful && response.body()!=null) {
                    try {
                        Log.e("getfield", response.body().toString())
                        //val ItemData = response.body() as FormSublist
                        FormPTList2 = response.body()!!

                        if(FormPTList2.size()>0)
                        {

                             //val keystatus = getKeyVaule(FormPTList2)
                             val keyList = getKeyName(FormPTList2)
                             /*val mJsonObject:JsonObject = FormPTList2.get(0) as JsonObject
                             Log.e("data",mJsonObject.toString())
                             val mFormPrimaryKey = mJsonObject.get("PrimaryKey")?.asString
                             val mFormSecondaryKey= mJsonObject.get("SecondaryKey")?.asString*/
                            val ItemList = ArrayList<String>()
                            FormPTList = ArrayList<FormSublistItem>()
                            when(keyList.first) {
                                2 -> {
                                    PrimaryKey_Name=keyList.second
                                    SecondaryKey_Name=keyList.third

                                    for (item in FormPTList2) {
                                        ItemList.add(item.asJsonObject[keyList.second].asString)
                                        FormPTList.add(FormSublistItem(
                                            item.asJsonObject[keyList.second].asString,
                                            item.asJsonObject[keyList.third].asString))
                                        /*FormPTList.add(
                                            FormSublistItem(item.asJsonObject["PT_Id"].asString,
                                                item.asJsonObject[PT_KEY_SubitemName].asString)
                                        )*/
                                    }

                                }

                                1 -> {
                                    PrimaryKey_Name=keyList.second
                                    for (item in FormPTList2) {
                                        ItemList.add(item.asJsonObject[keyList.second].asString)
                                        FormPTList.add(FormSublistItem(item.asJsonObject[keyList.second].asString))
                                    }

                                }
                                else ->{
                                    Log.e("Key","Primary Key Missing");
                                }
                            }

                            //set Adaptor
                            spinner.adapter = ArrayAdapter<String>(
                                this@DynamicForm,
                                android.R.layout.simple_spinner_item,
                                ItemList
                            )

                            spinner.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onNothingSelected(parent: AdapterView<*>?) {

                                    }

                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val PrimaryKeyValue  = parent?.getItemAtPosition(position).toString()
                                        Log.e("item", PrimaryKeyValue)

                                        PrimaryKey_value = PrimaryKeyValue
                                        SecondaryKey_value = null

                                        val data = FormPTList.filter { it.PT_Id == PrimaryKeyValue }.single()

                                        if (data.PT_Sub_key != null && !data.PT_Sub_key.isNullOrEmpty()) {
                                            val secondKeylist: List<String> = data.PT_Sub_key.split(',')
                                            getRunnerSpinnerItem(PrimaryKeyValue, secondKeylist)
                                            dynamicdataform.visibility = View.GONE


                                        } else {
                                            if (runnerFormDesign.isVisible) {
                                                runnerFormDesign.visibility = View.GONE
                                            }
                                            callGetFieldData(PrimaryKeyValue)
                                        }
                                    }

                                }
                        }else
                        {
                            Log.e("No List","Nodata")

//                            if(spinner.isVisible)
//                            {
//
//                            }
//                            if(mRunnerSpinner.isVisible)
//                            {
//                                runnerFormDesign.isv
//                            }

                        }

                    }catch (e:Exception)
                    {
                        Log.e("Nodata Error", e.message.toString())
                    }
                }else{
                    Log.e("Error code", response.code().toString())
                }
            }
            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })
    }


    /*private fun getSpinnerItem() {

        progressBar.visibility = View.VISIBLE

        val call2 = ApiClient.invoke().getItemListData(ObservationDataList(getCustomerId(), intent.getStringExtra("screen_no")!!))
        call2.enqueue(object : Callback<FormSublist> {
            override fun onResponse(call: Call<FormSublist>, response: Response<FormSublist>) {
                progressBar.visibility = View.GONE
                ////assert(response.body() != null)
                if(response.isSuccessful && response.body()!=null) {
                    try {
                        Log.e("getfield", response.body().toString())
                        //val ItemData = response.body() as FormSublist
                        FormPTList = response.body()!!
                        val ItemList= ArrayList<String>()
                        for(item in FormPTList)
                        {
                            ItemList.add(item.PT_Id)
                        }


                        //set Adaptor
                        spinner.adapter=ArrayAdapter<String>(this@DynamicForm,android.R.layout.simple_spinner_item,ItemList)

                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                val PT_ID = parent?.getItemAtPosition(position).toString()
                                Log.e("item",PT_ID)
                                Helper.PT_Id=PT_ID
                               // myMenu.findItem(R.menu.maser_details).setVisible(true);
                              //  myMenu.findItem(R.menu.maser_details).setEnabled(true);
                                PT_Run_No = null

                                val data = FormPTList.filter { it.PT_Id==PT_ID }.single()

                                if(data.Secondary_key!=null && !data.Secondary_key.isNullOrEmpty())
                                {
                                    val Secondary_keyList:List<String> = data.Secondary_key.split(',')

                                    getRunnerSpinnerItem(PT_ID,Secondary_keyList)
                                    dynamicdataform.visibility=View.GONE


                                }else {
                                    if(runnerFormDesign.isVisible)
                                    {
                                        runnerFormDesign.visibility=View.GONE
                                    }
                                    callGetFieldData(PT_ID)
                                }
                            }

                        }

                    }catch (e:Exception)
                    {
                        Log.e("Nodata Error", e.message.toString())
                    }
                }else{
                    Log.e("Error code", response.code().toString())
                }
            }
            override fun onFailure(call: Call<FormSublist?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })
    }*/

    @SuppressLint("ClickableViewAccessibility")
    private fun getRunnerSpinnerItem(PT_ID:String, subList:List<String>)
    {
        if(!runnerFormDesign.isVisible)
        {
            runnerFormDesign.visibility=View.VISIBLE
        }

        mRunnerSpinner = findViewById<View>(R.id.mRunner_spinner) as SearchableSpinner
        val mRunnerSpinnerAdapter = ArrayAdapter<String>(this@DynamicForm,android.R.layout.simple_spinner_item,subList)
        mRunnerSpinner.adapter = mRunnerSpinnerAdapter

        mRunnerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    val SecondaryKeyvalue = parent?.getItemAtPosition(position).toString()
                    Log.e("Runner No", SecondaryKeyvalue)
                   // PT_Run_No = Key
                    SecondaryKey_value=SecondaryKeyvalue

                    if (!dynamicdataform.isVisible) {
                        dynamicdataform.visibility = View.VISIBLE
                    }

                     callGetFieldData(PT_ID,SecondaryKey_value!!)
            }
        }
    }

    private fun callGetFieldData(PrimaryKeyValue:String,SecondaryKeyValue:String) {

        progressBar.visibility = View.VISIBLE

        val getFieldObject = JsonObject()
        getFieldObject.addProperty("custid", getCustomerId())
        getFieldObject.addProperty("ScreenNo", screen_no)
        getFieldObject.addProperty(PrimaryKey_Name, PrimaryKeyValue)
        if(SecondaryKey_Name!=null && SecondaryKeyValue!=null)
        getFieldObject.addProperty(SecondaryKey_Name, SecondaryKeyValue)


        val call2 = ApiClient.invoke().getFieldData(getFieldObject)
        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar.visibility = View.GONE
                if(response.isSuccessful && response.code()==200 && response.body() != null) {
                Log.e("getfield", response.body().toString())
                val FormData = MyFunctions.JsonArrayToJsonObject(response.body())
                callSelectFields(FormData)
             }else
            {
                Log.e("Failure", "Fail")
                showToast("Error  Code : "+ response.code()+" Try Again Later")
            }

            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })
    }

    /*private fun callGetFieldData(PrimaryKey_Value:String) {

        progressBar.visibility = View.VISIBLE

        val getFieldObject = JsonObject()
        getFieldObject.addProperty("custid", getCustomerId())
        getFieldObject.addProperty("ScreenNo", screen_no)
        getFieldObject.addProperty(PrimaryKey_Name, PrimaryKey_Value)

        //getFieldObject.addProperty("PT_Id", PT_ID)


        val call2 = ApiClient.invoke().getFieldData(getFieldObject)

        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.code()==200 && response.body() != null) {
                   // assert(response.body() != null)
                    Log.e("getfield", response.body().toString())
                    val FormData = MyFunctions.JsonArrayToJsonObject(response.body())
                    callSelectFields(FormData)
                }else
                {
                    Log.e("Failure", "Fail")
                    showToast("Error  Code : "+ response.code()+" Try Again Later")
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })


    }*/

    private fun callGetFieldData(PrimaryKey_Value:String) {

        progressBar.visibility = View.VISIBLE

        val getFieldObject = JsonObject()
        getFieldObject.addProperty("custid", getCustomerId())
        getFieldObject.addProperty("ScreenNo", screen_no)
        getFieldObject.addProperty(PrimaryKey_Name, PrimaryKey_Value)

        //getFieldObject.addProperty("PT_Id", PT_ID)


        val call2 = ApiClient.invoke().getFieldData(getFieldObject)

        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.code()==200 && response.body() != null) {
                    // assert(response.body() != null)
                    Log.e("getfield", response.body().toString())
                    val FormData = MyFunctions.JsonArrayToJsonObject(response.body())
                    callSelectFields(FormData)
                }else
                {
                    Log.e("Failure", "Fail")
                    showToast("Error  Code : "+ response.code()+" Try Again Later")
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })


    }

    private fun callUpdateFieldData(paramObject: JsonObject) {

       // progressBar.visibility = View.VISIBLE
        val gson = Gson()

        Log.e("checking", paramObject.toString())
        //val apiInterface = ApiClient.invoke()//.create(ApiInterface::class.java)
        val call2 = ApiClient().updateFieldData(paramObject)
        call2!!.enqueue(object : Callback<Boolean?> {
            override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                progressBar.visibility = View.GONE
                Log.e("rescode",response.code().toString() + " " + response.body().toString())
                //assert(response.body() != null)
                if (response.isSuccessful) {
                    if (response.body()!=null && response.body()==true) {
                        Toast.makeText(
                            this@DynamicForm,
                            "Form updated successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else Toast.makeText(
                        this@DynamicForm,
                        "Form update Failed",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.e("res",response.code().toString() + " " + response.body().toString())
                    showToast(response.code().toString() + " Internal Server Error")
                }
            }

            override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })

    }

    private fun callSelectFields(dataList: JsonObject?) {

        progressBar.visibility = View.VISIBLE
        //val apiInterface = ApiClient.invoke()//.create(ApiInterface::class.java)
        val call2 = ApiClient().selectFields(ScreenFormRequest(intent.getStringExtra("screen_no")))
        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar.visibility = View.GONE
               // button_layout.visibility = View.VISIBLE
                if(response.isSuccessful && response.code()==200 && response.body() != null) {
                    listFormData.clear()
                    listFormData = MyFunctions.JsonToModelField(response.body()) as ArrayList<FieldResponse>
                    if (!listFormData.isEmpty()) {
                        getDropDownFields(listFormData, dataList)
                    } else {
                        recycler_view.visibility = View.GONE
                    }
                }else
                {
                    Log.e("Failure", "Fail"+response.code()+" "+response.message())
                }
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })
    }




    private fun getDropDownFields(listFormData : ArrayList<FieldResponse>,dataList: JsonObject?)
    {
        progressBar.visibility = View.VISIBLE
        val call2 = ApiClient().getFormFieldLookup(ScreenNoRequest(screen_no))
        call2.enqueue(object : Callback<FormlookupResponse?> {
            override fun onResponse(call: Call<FormlookupResponse?>, response: Response<FormlookupResponse?>) {
                progressBar.visibility = View.GONE
                button_layout.visibility = View.VISIBLE

                if(response.code()==200 && response.body()!=null) {
                    listFormDropdownData.clear()
                    listFormDropdownData = response.body()!!
                    if(listFormDropdownData.size>0) {
                        listDropdownData.clear()

                        val namelist = listFormDropdownData.distinctBy { it.Field_Name }

                        for(uniqueName in namelist)
                        {
                            val list_name = ArrayList<String>()

                            for(x in listFormDropdownData)
                            {
                                if(x.Field_Name.equals(uniqueName.Field_Name))
                                {
                                    list_name.add(x.Combo_list)
                                }
                            }

                            listDropdownData.add(FormDropdownwithlist(uniqueName.Field_Name,list_name))
                        }


                        /*for(xyz in listDropdownData)
                        {
                            Log.e("FilterData",xyz.fieldName+"-"+xyz.fieldList.toString())
                        }*/
                        recycler_view.visibility = View.VISIBLE
                        listAdapter = DynamicFormAdapter(this@DynamicForm, listFormData, dataList,listDropdownData)
                        recycler_view.adapter = listAdapter
                    }
                    else
                    {
                        listDropdownData.clear()
                        recycler_view.visibility = View.VISIBLE
                        listAdapter = DynamicFormAdapter(this@DynamicForm, listFormData, dataList,listDropdownData)
                        recycler_view.adapter = listAdapter
                    }
                }else
                {
                    Log.e("Failure", "Fail"+response.code()+" "+response.message())
                }
            }

            override fun onFailure(call: Call<FormlookupResponse?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })

    }

    override fun onPause() {
        super.onPause()
        if(db.isOpen && !db.inTransaction())
        {
            db.close()
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.maser_details, menu)
       // myMenu = menu
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        //Log.e("data",item.itemId.toString()+"-"+R.id.action_search.toString()+R.id.action_logout.toString())
        return when (item.itemId) {

            R.id.action_master -> {
                Log.e("PrimaryKeyValue",PrimaryKey_value.toString())

                if(PrimaryKey_value.isNullOrEmpty() || PrimaryKey_value.isNullOrBlank())
                {
                    Log.e("PrimaryKeyName",PrimaryKey_Name.toString())
                    showMsg("Please Select the Item")
                    return true
                }
                else if(PrimaryKey_value!=null && !PrimaryKey_value.equals("null")) {
                    Log.e("PT_Id",false.toString())
                    val i = Intent(this, Machinematerdata::class.java)
                    i.putExtra("category_name", intent.getStringExtra("category_name")!!)
                    i.putExtra("screen_no", intent.getStringExtra("screen_no")!!)
                    i.putExtra("key_name", PrimaryKey_Name)
                    i.putExtra("key_value", PrimaryKey_value)

                    Log.e(
                        "data",
                        intent.getStringExtra("category_name")!! + "-" + intent.getStringExtra("screen_no")!! + "-"
                    )
                    startActivity(i)
                    true
                } else {
                    Log.e("PT_Id",false.toString())
                    //showToast("PT_Id Value is Empty")
                    showMsg("Please Select the Item")
                    return super.onOptionsItemSelected(item)
                }
            }

            else ->  return super.onOptionsItemSelected(item)
        }
    }

    /*override fun onSupportNavigateUp(): Boolean {
       // onNavigateUp()
        return onNavigateUp()
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp()
    }*/

    fun showMsg(Msg:String)
    {
        showToast(Msg)
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}