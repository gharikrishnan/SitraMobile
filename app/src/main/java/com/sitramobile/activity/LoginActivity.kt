package com.sitramobile.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sitramobile.R
import com.sitramobile.activity.LoginActivity
import com.sitramobile.api.ApiClient
import com.sitramobile.api.ApiInterface
import com.sitramobile.modelRequest.LoginRequest
import com.sitramobile.modelResponse.LoginResponse
import com.sitramobile.utils.Constants
import com.sitramobile.utils.Coroutines
import com.sitramobile.utils.Helper
import com.sitramobile.utils.Helper.setUserData
import com.sitramobile.utils.MyFunctions
import com.sitramobile.utils.ViewUtils.isInternetAvailable
import com.sitramobile.utils.ViewUtils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

class LoginActivity : AppCompatActivity() {
    var user_layout: TextInputLayout? = null
    var pass_layout: TextInputLayout? = null
    var user_name: TextInputEditText? = null
    var password: TextInputEditText? = null
    var login: Button? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        user_layout = findViewById<View>(R.id.user_layout) as TextInputLayout?
        pass_layout = findViewById<View>(R.id.pass_layout) as TextInputLayout?
        user_name = findViewById<View>(R.id.user_name) as TextInputEditText?
        password = findViewById<View>(R.id.password) as TextInputEditText?
        login = findViewById<View>(R.id.login) as Button?
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar?



        getValue()

        user_name!!.addTextChangedListener(object : TextWatcher {
            public override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            public override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            public override fun afterTextChanged(s: Editable) {
                user_layout!!.setError(null)
                user_layout!!.setErrorEnabled(false)
            }
        })
        password!!.addTextChangedListener(object : TextWatcher {
            public override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            public override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            public override fun afterTextChanged(s: Editable) {
                pass_layout!!.error = null
                pass_layout!!.isErrorEnabled = false
            }
        })
        login!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                if (validation()) {
                    callLogin()
                }
            }
        })


    }

    private fun getValue()
    {
        try {
            val value: String? = MyFunctions.getStringSharedPref(this@LoginActivity, Constants.PRODUCT_KEY )
            if (value != null) {
                Log.e("Key_Success","$value")
            }
            else{
                Log.e("Key_Null","NULL")
            }
        }
        catch (e:Exception)
        {
            Log.e("Key_success",e.toString())
        }
    }

    private fun validation(): Boolean {
        if (user_name!!.text.toString().isEmpty()) {
            user_layout!!.setError("You need to enter a username")
            return false
        }
        if (password!!.getText().toString().isEmpty()) {
            pass_layout!!.setError("You need to enter a password")
            return false
        }
        return true
    }

    private fun callLogin() {

        progressBar!!.setVisibility(View.VISIBLE)
        val username= user_name!!.getText().toString().trim { it <= ' ' }
        val password= password!!.getText().toString().trim { it <= ' ' }

        if(isInternetAvailable()) {
            try {
                val call2: Call<LoginResponse> = ApiClient().login(LoginRequest(username, password))
                call2.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        if (response.isSuccessful && response.code() == 200) {

                            progressBar!!.setVisibility(View.GONE)

                            if (response.body()!=null && response.body()!!.status==true){//response.body()!= null && response.body().status == true) {
                                val user = response.body()!!
                                MyFunctions.setStringSharedPref(
                                    this@LoginActivity,
                                    Constants.USER_ID,
                                    user.id
                                )
                                MyFunctions.setStringSharedPref(
                                    this@LoginActivity,
                                    Constants.USER_ROLE,
                                    user.Role
                                )

                                setUserData(username,user.id,user.Role)

                                startActivity(Intent(this@LoginActivity, CustomerList::class.java))
                                finish()
                            } else if(response.body()!=null) {
                                showToast(response.body()!!.message)
                            }else
                            {
                                showToast("Data Not Found")
                            }
                        } else {
                            progressBar!!.setVisibility(View.GONE)
                            Log.e("data", response.body()!!.status.toString())
                            showToast(response.code().toString() + " Internal Server Error ")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        progressBar!!.setVisibility(View.GONE)
                        showToast("Please Check Internet Connection")
                        Log.e("Failure", "Fail")
                    }
                })
            } catch (x: Exception) {

                Log.e("Error", x.toString())
                //catch app crash
                when (x) {
                    is ConnectException -> Coroutines.main {
                        progressBar!!.setVisibility(View.GONE)
                        showToast("Please Check Internet Connection")
                    }
                    is UnknownHostException -> Coroutines.main {
                        progressBar!!.setVisibility(View.GONE)
                        showToast("Please Check Internet Connection")
                    }
                    else -> { // Note the block
                        Coroutines.main {
                            progressBar!!.setVisibility(View.GONE)
                            showToast("Please Try Again Later")
                        }
                    }
                }
            }
        }else
        {
            progressBar!!.setVisibility(View.GONE)
            showToast("Please Check Internet Connection")
        }
    }
}