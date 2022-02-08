package com.sitramobile.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.sitramobile.R
import com.sitramobile.adapter.CategoryListAdapter
import com.sitramobile.api.ApiClient
import com.sitramobile.modelRequest.SelectScreenRequest
import com.sitramobile.modelResponse.CategoryModel
import com.sitramobile.utils.Constants
import com.sitramobile.utils.Helper.getUserName
import com.sitramobile.utils.MyFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CategoryMenuList : AppCompatActivity() {

    var mToolbar: Toolbar? = null
    var empty_icon: ImageView? = null
    var empty_txt: TextView? = null
    var recycler_view: RecyclerView? = null
    var listAdapter: CategoryListAdapter? = null
    var progressBar: ProgressBar? = null
    var list_data: MutableList<CategoryModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_menu_list)
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val mUsername = findViewById<View>(R.id.user_name) as TextView
        mToolbar!!.title="Category List"
        mUsername.setText("Username : "+getUserName())
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        empty_icon = findViewById<View>(R.id.empty_icon) as ImageView
        empty_txt = findViewById<View>(R.id.empty_txt) as TextView
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        recycler_view = findViewById<View>(R.id.recycler_view) as RecyclerView

        /*val layoutManager = GridLayoutManager(this, 2)
        layoutManager.orientation = RecyclerView.VERTICAL.
        recycler_view!!.layoutManager = layoutManager*/

        callCategoryList()
        //  addCategoryToList();
    }



    private fun callCategoryList() {
        /*
        this function is calling menu list
        for category of item
         */
        progressBar!!.visibility = View.VISIBLE
        //val apiInterface = ApiClient.client.create(ApiInterface::class.java)
        val call2 = ApiClient().categoryMenuList(
            SelectScreenRequest(
                MyFunctions.getStringSharedPref(
                    this@CategoryMenuList,
                    Constants.USER_ID
                )
            )
        )
        call2!!.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(call: Call<JsonArray?>, response: Response<JsonArray?>) {
                progressBar!!.visibility = View.GONE
                list_data.clear()
                if (response.isSuccessful) {
                    val listData = MyFunctions.JsonToModelCategoryList(response.body())
                    if (!listData.isEmpty()) {

                        recycler_view!!.visibility = View.VISIBLE
                        empty_txt!!.visibility = View.GONE
                        empty_icon!!.visibility = View.GONE
                        listAdapter = CategoryListAdapter(this@CategoryMenuList, listData)
                        recycler_view!!.adapter = listAdapter

                    } else {
                        recycler_view!!.visibility = View.GONE
                        empty_txt!!.visibility = View.VISIBLE
                        empty_icon!!.visibility = View.VISIBLE
                    }
                } else Toast.makeText(
                    this@CategoryMenuList,
                    response.code().toString() + " Internal Server Error",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<JsonArray?>, t: Throwable) {
                progressBar!!.visibility = View.GONE
                Log.e("Failure", "Fail")
            }
        })
    }

    /*
    private void addCategoryToList() {
        CategoryModel categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.1);
        categoryModel.setCategory_name("Power Transformer Operating Conditions");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.2);
        categoryModel.setCategory_name("PT Location Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.3);
        categoryModel.setCategory_name("PT OLTC Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.4);
        categoryModel.setCategory_name("PT Measured Voltage");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.5);
        categoryModel.setCategory_name("PT Measured Voltage Reading");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.6);
        categoryModel.setCategory_name("PT Measured Current");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.7);
        categoryModel.setCategory_name("PT Measured Current Reading");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.8);
        categoryModel.setCategory_name("PT Surface Temperature");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.9);
        categoryModel.setCategory_name("PT Vibration Measurement");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.10);
        categoryModel.setCategory_name("PT Load Analysis");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.11);
        categoryModel.setCategory_name("Distribution Transformer Operating Conditions");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.12);
        categoryModel.setCategory_name("DT Location Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.13);
        categoryModel.setCategory_name("DT OLTC Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.14);
        categoryModel.setCategory_name("DT Measured Voltage");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.15);
        categoryModel.setCategory_name("DT Measured Voltage Reading");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.16);
        categoryModel.setCategory_name("DT Measured Current");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.17);
        categoryModel.setCategory_name("DT Measured Current Reading");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.18);
        categoryModel.setCategory_name("DT Surface Temperature");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.19);
        categoryModel.setCategory_name("DT Vibration Measurement");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.20);
        categoryModel.setCategory_name("DT Load Analysis");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.21);
        categoryModel.setCategory_name("Feeder Cable Loss Calc");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.22);
        categoryModel.setCategory_name("Feeder Cable Loss Reading");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.23);
        categoryModel.setCategory_name("Feeder Analysis Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.24);
        categoryModel.setCategory_name("Feeder Analysis General Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.25);
        categoryModel.setCategory_name("Feeder Harmonics Order");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.26);
        categoryModel.setCategory_name("Feeder Load and Power Quality Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.27);
        categoryModel.setCategory_name("AWES Motor Loading");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.28);
        categoryModel.setCategory_name("Humidication Motor Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.29);
        categoryModel.setCategory_name("Air Compressor Observation");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.30);
        categoryModel.setCategory_name("Dryer Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.31);
        categoryModel.setCategory_name("Boiler Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.32);
        categoryModel.setCategory_name("Thermopac Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.33);
        categoryModel.setCategory_name("MV Panel Meters and Controls");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.34);
        categoryModel.setCategory_name("MV Panel Temperature at Hot Switches");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.35);
        categoryModel.setCategory_name("MV Panel Temperature Measurement at Incomer");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.36);
        categoryModel.setCategory_name("Capacitor Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.37);
        categoryModel.setCategory_name("Generators - Auxiliaries");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.38);
        categoryModel.setCategory_name("Compressor - Accessories");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.39);
        categoryModel.setCategory_name("Yarn Conditioning Plant Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.40);
        categoryModel.setCategory_name("Full load measurements");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.41);
        categoryModel.setCategory_name("No Load Measurements");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.42);
        categoryModel.setCategory_name("Chiller Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.43);
        categoryModel.setCategory_name("Supply Air Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.44);
        categoryModel.setCategory_name("Exhaust System Observation Data");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.45);
        categoryModel.setCategory_name("Department-wise Analysis");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.46);
        categoryModel.setCategory_name("Dept-wise Consumption - Ring Spg");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.47);
        categoryModel.setCategory_name("Dept-wise Consumption - Rotor Spg");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.48);
        categoryModel.setCategory_name("Dept-wise Consumption - Vortex Spg");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.49);
        categoryModel.setCategory_name("Lighting Spg , Dlg and Looms");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.50);
        categoryModel.setCategory_name("Lighting other Departments");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.51);
        categoryModel.setCategory_name("Harmonic Study - Location Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.52);
        categoryModel.setCategory_name("Harmonic Study - Load and Power Quality Details");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(1.53);
        categoryModel.setCategory_name("Harmonics Study - Order");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(2.1);
        categoryModel.setCategory_name("Plant Capacity Analysis- Supply");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(2.2);
        categoryModel.setCategory_name("Plant Capacity Analysis- Exhaust");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(3.1);
        categoryModel.setCategory_name("Pneumafil Suction Pressure - RF & FF");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(3.2);
        categoryModel.setCategory_name("Compact Suction Pressure - RF & FF");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(3.3);
        categoryModel.setCategory_name("Cards - Suction Pressure at AWES");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(3.4);
        categoryModel.setCategory_name("Comber  Suction  Pressure at AWES");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(3.5);
        categoryModel.setCategory_name("Auto Coner - Suction Pressure");
        list_data.add(categoryModel);

        categoryModel =new CategoryModel();
        categoryModel.setCategory_id(3.6);
        categoryModel.setCategory_name("Full Cop Weight and Diameter Checking");
        list_data.add(categoryModel);

        listAdapter = new CategoryListAdapter(CategoryMenuList.this, list_data);
        recycler_view.setAdapter(listAdapter);
    }
*/
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}