package com.sitramobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sitramobile.R
import com.sitramobile.utils.Constants
import com.sitramobile.utils.MyFunctions

class ProductKey : AppCompatActivity() {
    var keyLayout: TextInputLayout? = null
    var keyValue: TextInputEditText? = null
    var login: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        /* this is product key activity get product key automatically ,after open the application automatically
         redirect to login activity
         */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_key)

        keyLayout = findViewById(R.id.keys_layout)
        keyValue = findViewById(R.id.keys_value )
        login = findViewById(R.id.submit)

        keyValue!!.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                keyLayout!!.error = null
                keyLayout!!.isErrorEnabled = false
            }

        })

        login!!.setOnClickListener{


            if (validation())
            {
                callLogin()
            }
        }



        }

    private fun callLogin() {

            startActivity(Intent(this@ProductKey,LoginActivity::class.java))
            finish()

    }

    private fun keyValidation(productKey: String) {


        try {
            Log.e("Values2", " $productKey")
            MyFunctions.setStringSharedPref(
                this@ProductKey,
                Constants.PRODUCT_KEY, productKey)
            Log.e("INSERT_SUCCESS","Success")

        }
        catch (e :Exception)
        {
            Log.e("INSERT_ERROR","$e")
        }


    }

    private fun validation(): Boolean {
        if(keyValue!!.text.toString().isEmpty() )
        {
            keyLayout!!.error = "You Need Enter Product Key"
            return false
        }
        if(keyValue!!.length() < 10)
        {
            keyLayout!!.error = "Enter valid Product Key"
            return false
        }
        if(keyValue!!.length() == 10)
        {
            val productKey: String = keyValue!!.text.toString().trim()
            Log.e("Values1", " $productKey")
            keyValidation(productKey)
        }
        return true
    }


}
