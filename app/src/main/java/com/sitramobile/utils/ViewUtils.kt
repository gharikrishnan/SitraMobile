package com.sitramobile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object ViewUtils {


    fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("ServiceCast")
    fun Context.isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else
            {
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
                return isConnected
            }
        }
        return result
    }



    fun getKeyVaule(arrayList: JsonArray):Pair<Boolean,List<String>?>
    {
        val objectKeyname = arrayList.get(0).asJsonObject

        Log.e("getKeyVauleFun",objectKeyname.isJsonObject.toString())

        if(objectKeyname.isJsonObject) {
            val keys = objectKeyname.keySet().toList()
            if(objectKeyname.keySet().size>1) {
                Log.e("key",keys.toString())
                return Pair(true, keys.toMutableList())
            }else
            {
                return Pair(false, null)
            }
        }
        else  {
            return Pair(false,null)
        }
    }

    fun getKeyName(arrayList: JsonArray):Triple<Int,String?,String?>
    {
        val mJsonObject: JsonObject = arrayList.get(0) as JsonObject

        val mFormPrimaryKey = mJsonObject.get("PrimaryKey")?.asString
        val mFormSecondaryKey= mJsonObject.get("SecondaryKey")?.asString

        if(!mFormPrimaryKey.isNullOrEmpty() && !mFormSecondaryKey.isNullOrEmpty())
        {
            return Triple(2, mFormPrimaryKey.trim(),mFormSecondaryKey.trim())
        }else if(!mFormPrimaryKey.isNullOrEmpty()){
            return Triple(1, mFormPrimaryKey.trim(),null)
        }else
        {
            return Triple(0, null,null)
        }
    }



}