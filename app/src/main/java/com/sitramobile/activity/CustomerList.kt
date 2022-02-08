package com.sitramobile.activity

import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.sitramobile.R
import com.sitramobile.activity.CustomerList
import com.sitramobile.adapter.CustomerListAdapter
import com.sitramobile.api.ApiClient
import com.sitramobile.modelResponse.CustomerModel
import com.sitramobile.utils.Helper.getCustomer
import com.sitramobile.utils.Helper.getUserName
import com.sitramobile.utils.Helper.logoutUser
import com.sitramobile.utils.Helper.setCutomerlist
import com.sitramobile.utils.ViewUtils.isInternetAvailable
import com.sitramobile.utils.ViewUtils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CustomerList constructor() : AppCompatActivity() {

    /* this is intiatization
    and declaration
     */
    var mToolbar: Toolbar? = null
    var empty_icon: ImageView? = null
    var empty_txt: TextView? = null
    var swipe_refresh: SwipeRefreshLayout? = null
    var recycler_view: RecyclerView? = null
    var listAdapter: CustomerListAdapter? = null
    var progressBar: ProgressBar? = null
    //var list_data: MutableList<CustomerModel> = ArrayList()
    var list_data: MutableList<CustomerModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar?
        val mUsername = findViewById<View>(R.id.user_name) as TextView
        mToolbar!!.title="Customers List"
        mUsername.setText("Username : "+getUserName())

        setSupportActionBar(mToolbar)

        swipe_refresh = findViewById<View>(R.id.swipe_refresh) as SwipeRefreshLayout?
        recycler_view = findViewById<View>(R.id.recycler_view) as RecyclerView?
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar?
        empty_icon = findViewById<View>(R.id.empty_icon) as ImageView?
        empty_txt = findViewById<View>(R.id.empty_txt) as TextView?
        swipe_refresh!!.setOnRefreshListener(object : OnRefreshListener {
            public override fun onRefresh() {
                callCustomerList()
            }
        })
        /*val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        layoutManager.setOrientation(RecyclerView.VERTICAL)
        recycler_view!!.setLayoutManager(layoutManager)*/
        callCustomerList()
       /* if(isInternetAvailable()){ callCustomerList()}
        else {
            val list = getCustomer()
            if(list.size>0) {
                recycler_view!!.setVisibility(View.VISIBLE)
                empty_txt!!.setVisibility(View.GONE)
                empty_icon!!.setVisibility(View.GONE)
                listAdapter = CustomerListAdapter(this@CustomerList, list)
                recycler_view!!.setAdapter(listAdapter)
            }
        }*/
    }


    private fun callCustomerList() {
        /* this function is used to call http
     request using retrofit
      */

        progressBar!!.setVisibility(View.VISIBLE)
        if(isInternetAvailable()) {
            //val apiInterface: ApiInterface = ApiClient().create(ApiInterface::class.java)
            val call2: Call<List<CustomerModel>> = ApiClient().customerList()
            call2.enqueue(object : Callback<List<CustomerModel>> {
                override fun onResponse(
                    call: Call<List<CustomerModel>>,
                    response: Response<List<CustomerModel>>
                ) {
                    swipe_refresh!!.setRefreshing(false)
                    progressBar!!.setVisibility(View.GONE)
                    //list_data.clear()
                    //list_data = response.body() as MutableList<CustomerModel>
                    if(response.isSuccessful && response.body()!=null)
                    list_data.clear()
                    list_data = response.body() as MutableList<CustomerModel> //MyFunctions.JsonToModelCustomerList(response.body())
                    if (!list_data.isNullOrEmpty()) {
                        setCutomerlist(list_data)
                        recycler_view!!.setVisibility(View.VISIBLE)
                        empty_txt!!.setVisibility(View.GONE)
                        empty_icon!!.setVisibility(View.GONE)
                        listAdapter = CustomerListAdapter(this@CustomerList, list_data)
                        recycler_view!!.setAdapter(listAdapter)
                    } else {
                        recycler_view!!.setVisibility(View.GONE)
                        empty_txt!!.setVisibility(View.VISIBLE)
                        empty_icon!!.setVisibility(View.VISIBLE)
                    }
                }

                override fun onFailure(call: Call<List<CustomerModel>>, t: Throwable)
                {
                    swipe_refresh!!.setRefreshing(false)
                    progressBar!!.setVisibility(View.GONE)
                    Log.e("Failure", "Fail")
                }
            })
        }else
        {
            swipe_refresh!!.setRefreshing(false)
            progressBar!!.setVisibility(View.GONE)
            showToast("Please Check Internet Connection")
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.customer_search, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        //Log.e("data",item.itemId.toString()+"-"+R.id.action_search.toString()+R.id.action_logout.toString())
        return when (item.itemId) {
            R.id.action_search -> {
                val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView
                val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
                searchView.setMaxWidth(Int.MAX_VALUE)
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()))
                //searchView.query
                if(!list_data.isEmpty()) {
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(query: String): Boolean {
                            val list = list_data.filter {

                                it.name?.toLowerCase()?.contains(query.toLowerCase()) == true
                                        || it.address1?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.phone?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.email?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.address2?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.city?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.pincode?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.state?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                            }
                            listAdapter = CustomerListAdapter(this@CustomerList, list)
                            recycler_view!!.setAdapter(listAdapter)
                            searchView.isFocusable = false
                            return false
                        }

                        override fun onQueryTextSubmit(query: String): Boolean {
                            // task HERE
                            //on submit send entire query
                            val list = list_data.filter {

                                it.name?.toLowerCase()?.contains(query.toLowerCase()) == true
                                        || it.address1?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.phone?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.email?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.address2?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.city?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.pincode?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                                        || it.state?.toLowerCase()
                                    ?.contains(query.toLowerCase()) == true
                            }
                            listAdapter = CustomerListAdapter(this@CustomerList, list)
                            recycler_view!!.setAdapter(listAdapter)

                            return false
                        }


                    })
                    searchView.setOnCloseListener(object :
                        androidx.appcompat.widget.SearchView.OnCloseListener {
                        override fun onClose(): Boolean {

                            searchView.setIconifiedByDefault(true)
                            searchView.visibility = View.GONE
                            recycler_view!!.setVisibility(View.VISIBLE)
                            empty_txt!!.setVisibility(View.GONE)
                            empty_icon!!.setVisibility(View.GONE)
                            listAdapter = CustomerListAdapter(this@CustomerList, list_data)
                            recycler_view!!.setAdapter(listAdapter)
                            recycler_view!!.adapter?.notifyDataSetChanged()

                            return true
                        }

                    })
                }
                true
            }
            R.id.action_logout -> {
                Log.e("Logout","Logout")
                Logout()
                true
            }
            else ->  true //return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        back_Exit()
    }

    fun back_Exit()
    {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Exit the App?")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to exit?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
            this.finishAffinity()
        }
       /* //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }*/
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()

        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    fun Logout()
    {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Logout the App?")
        //set message for alert dialog
        builder.setMessage("Are you sure, you want to logout?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
            logoutUser()
        }
        /* //performing cancel action
         builder.setNeutralButton("Cancel"){dialogInterface , which ->
             Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
         }*/
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()

        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()


    }


}