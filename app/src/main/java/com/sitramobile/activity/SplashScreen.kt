package com.sitramobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sitramobile.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sitramobile.utils.Constants
import com.sitramobile.utils.Helper.getCustomerId
import com.sitramobile.utils.MyFunctions

class SplashScreen : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({

            try {
                    val value: String? = MyFunctions.getStringSharedPref(this@SplashScreen, Constants.PRODUCT_KEY )
                    if (value != null) {
                        Log.e("Key_Success","$value")
                    }
                    else{
                        Log.e("Key_Null","NULL")
                    }
                }
                catch (e:Exception)
                {
                    Log.e("Key_success","$e")
                }


            when {
                MyFunctions.getStringSharedPref(this@SplashScreen, Constants.PRODUCT_KEY )?.isEmpty() == true -> {
                    startActivity(Intent(this@SplashScreen, ProductKey::class.java))
                    finish()
                }
                getCustomerId().isEmpty() -> {
                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this@SplashScreen, CustomerList::class.java))
                    finish()
                }
            } }, SPLASH_DISPLAY_LENGTH.toLong())


    }
}

