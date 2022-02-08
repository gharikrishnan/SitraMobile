package com.sitramobile.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sitramobile.R
import com.sitramobile.adapter.DynamicFormAdapter
import com.sitramobile.adapter.MasterDataFormAdapter
import com.sitramobile.api.ApiClient
import com.sitramobile.db.AppDatabase
import com.sitramobile.db.DynamicData
import com.sitramobile.modelRequest.MasterDataRequest
import com.sitramobile.modelRequest.Masterdropwithlist
import com.sitramobile.modelRequest.ScreenNoRequest
import com.sitramobile.modelResponse.FieldResponse
import com.sitramobile.modelResponse.MasterlookupResponse
import com.sitramobile.modelResponse.MasterlookupResponseItem
import com.sitramobile.utils.Constants
import com.sitramobile.utils.Coroutines
import com.sitramobile.utils.Helper.getCustomerId
import com.sitramobile.utils.Helper.getUserId
import com.sitramobile.utils.Helper.getUserName
import com.sitramobile.utils.MyFunctions
import com.sitramobile.utils.ViewUtils.isInternetAvailable
import com.sitramobile.utils.ViewUtils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Machinematerdata : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    lateinit var recycler_view: RecyclerView
    lateinit var listAdapter : MasterDataFormAdapter
    lateinit var progressBar: ProgressBar
    lateinit var button_layout: LinearLayout
    lateinit var cancel: Button
    lateinit var save: Button
    var screen_no:String = "";


    var listFormData : ArrayList<FieldResponse> = ArrayList<FieldResponse>()

    var listMasterDropdownData : ArrayList<MasterlookupResponseItem> = ArrayList<MasterlookupResponseItem>()
    var listDropdownData : ArrayList<Masterdropwithlist> = ArrayList<Masterdropwithlist>()

    var PrimaryKey_Name:String ?=null
    var PrimaryKey_value:String ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machinematerdata)

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val mUsername = findViewById<View>(R.id.user_name) as TextView

        mUsername.setText("Username : "+getUserName())
        //mToolbar.title = intent.getStringExtra("category_name")!!
        mToolbar.title = "Master Data Form"
        screen_no = intent.getStringExtra("screen_no")!!
        //PT_Id = intent.getStringExtra("PT_Id")!!
        PrimaryKey_Name = intent.getStringExtra("key_name")!!
        PrimaryKey_value = intent.getStringExtra("key_value")!!

        //db = AppDatabase.invoke(this)

        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        recycler_view = findViewById<View>(R.id.recycler_view) as RecyclerView
        button_layout = findViewById<View>(R.id.button_layout) as LinearLayout
        cancel = findViewById<View>(R.id.cancel) as Button
        save = findViewById<View>(R.id.save) as Button

        cancel.setOnClickListener { finish() }

        save.setOnClickListener(View.OnClickListener {

            val paramObject = JsonObject()
            paramObject.addProperty("custid", getCustomerId())
            paramObject.addProperty("ScreenNo", screen_no)
            paramObject.addProperty(PrimaryKey_Name, PrimaryKey_value)

            for (model in listFormData) {
                paramObject.addProperty(model.Field_Name, model.fieldValue)
                if (model.Mandatory == "1" && model.fieldValue.isNullOrEmpty()){
                    showDialog()
                    return@OnClickListener
                }
            }

            Log.e("checking", paramObject.toString())

            if(isInternetAvailable()) {

                Log.e("internet","Available")
                callUpdateFieldData(paramObject)

            }/*else {
                Log.e("internet", "Unavailable")

                Coroutines.io {
                    insertOrUpdate( DynamicData(
                        paramObject["custid"].toString(),
                        paramObject["ScreenNo"].toString(),
                        paramObject.toString()
                    )
                    )
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
            }*/
        })

        callGetFieldData()
        /*if (this.isInternetAvailable()) {
            var i=0;
            var mOfflineData:List<DynamicData> = mutableListOf()
            Coroutines.io {
                mOfflineData = db.getDynamicDataDao().getAllDynamicData()
                Coroutines.main {
                    i = mOfflineData.size
                    if (i == 0) {
                        callGetFieldData()
                    } else {
                        showSyncDialog(mOfflineData)
                    }
                }
            }
        } else {
            showToast("Please Check Internet Connection")

        }*/

    }

   /* suspend fun insertOrUpdate(item: DynamicData) {
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
    }*/



    private fun showDialog() {

        val dialog = Dialog(this@Machinematerdata, R.style.CustomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.error_alert)
        val close = dialog.findViewById<ImageView>(R.id.close_alert)
        close.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    /*private fun showSyncDialog(mOfflineData:List<DynamicData>) {

        mSyncDialog = Dialog(this@DynamicForm, R.style.CustomDialog)
        mSyncDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mSyncDialog.setCancelable(true)
        mSyncDialog.setContentView(R.layout.sync_now)
        val SyncData = mSyncDialog.findViewById<Button>(R.id.bnt_sync)
        SyncData.setOnClickListener {
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
                                    this@Machinematerdata,
                                    "Form updated successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                Coroutines.io {
                                    db.getDynamicDataDao().delete(j)
                                    Log.e("delected","successfully")
                                }

                            } else Toast.makeText(
                                this@Machinematerdata,
                                "Form update Failed",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@Machinematerdata,
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
            mSyncDialog.dismiss()
        }
        mSyncDialog.show()

    }*/

    private fun callGetFieldData() {

        progressBar.visibility = View.VISIBLE

        val paramObject = JsonObject()
        paramObject.addProperty("custid", getCustomerId())
        paramObject.addProperty("ScreenNo", screen_no)
        paramObject.addProperty(PrimaryKey_Name, PrimaryKey_value)

        Log.e("param", Gson().toJson(paramObject))

        val call2 = ApiClient().getMasterData(paramObject)

        //val call2 = ApiClient().getMasterData(MasterDataRequest(custid=getCustomerId(),ScreenNo=screen_no,PT_id="1"))
        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar.visibility = View.GONE
                if(response.isSuccessful && response.code()==200 && response.body() != null) {
                    Log.e("getfield", response.body().toString())

                    val FormData = MyFunctions.JsonArrayToJsonObject(response.body())

                    callSelectFields(FormData)
                }
                else
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

        progressBar.visibility = View.VISIBLE

        val gson = Gson()

        Log.e("checking", gson.toJson(listFormData))
        Log.e("checking-2", gson.toJson(paramObject))
        //val apiInterface = ApiClient.invoke()//.create(ApiInterface::class.java)

        val call2 = ApiClient().updateMasterFieldData(paramObject)
        call2!!.enqueue(object : Callback<Boolean?> {
            override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                progressBar.visibility = View.GONE
                Log.e("rescode",response.code().toString() + " " + response.body().toString())
                //assert(response.body() != null)
                if (response.isSuccessful) {
                    if (response.body()!=null && response.body()==true) {
                        Toast.makeText(
                            this@Machinematerdata,
                            "Master Data updated successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else Toast.makeText(
                        this@Machinematerdata,
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
        val call2 = ApiClient().getMasterFieldData(ScreenNoRequest(screen_no))
        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar.visibility = View.GONE
                //button_layout.visibility = View.VISIBLE
                if(response.isSuccessful && response.code()==200 && response.body() != null) {
                    listFormData.clear()
                    listFormData = MyFunctions.JsonToModelField(response.body()) as ArrayList<FieldResponse>
                    if (!listFormData.isEmpty()) {

                        //Log.e("getMasterselectfield", listFormData.toString())
                        //recycler_view.visibility = View.VISIBLE
                        //listAdapter = MasterDataFormAdapter(this@Machinematerdata, listFormData, dataList)
                        //recycler_view.adapter = listAdapter
                        getDropDownFields(listFormData, dataList)

                    } else {
                        recycler_view.visibility = View.GONE
                    }
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

    private fun getDropDownFields(listFormData : ArrayList<FieldResponse>,dataList: JsonObject?)
    {
        progressBar.visibility = View.VISIBLE
        val call2 = ApiClient().getMasterFieldLookup(ScreenNoRequest(screen_no))
        call2.enqueue(object : Callback<MasterlookupResponse?> {
            override fun onResponse(call: Call<MasterlookupResponse?>, response: Response<MasterlookupResponse?>) {
                progressBar.visibility = View.GONE
                button_layout.visibility = View.VISIBLE

                if(response.code()==200 && response.body()!=null) {
                    listMasterDropdownData.clear()
                    listMasterDropdownData = response.body()!!
                    if(listMasterDropdownData.size>0) {
                        listDropdownData.clear()
                        val namelist = listMasterDropdownData.distinctBy { it.Field_Name }

                            for(uniqueName in namelist)
                            {
                                val list_name = ArrayList<String>()

                                for(x in listMasterDropdownData)
                                {
                                    if(x.Field_Name.equals(uniqueName.Field_Name))
                                    {
                                        list_name.add(x.Combo_list)
                                    }
                                }

                                listDropdownData.add(Masterdropwithlist(uniqueName.Field_Name,list_name))
                            }

                        /*for(xyz in listDropdownData)
                        {
                            Log.e("FilterData",xyz.fieldName+"-"+xyz.fieldList.toString())
                        }*/

                        recycler_view.visibility = View.VISIBLE
                        listAdapter = MasterDataFormAdapter(this@Machinematerdata, listFormData, dataList,listDropdownData)
                        recycler_view.adapter = listAdapter
                    }
                }else
                {
                    Log.e("Failure", "Fail"+response.code()+" "+response.message())
                }
            }

            override fun onFailure(call: Call<MasterlookupResponse?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })

    }

    override fun onPause() {
        super.onPause()
//        if(db.isOpen && !db.inTransaction())
//        {
//            db.close()
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


}