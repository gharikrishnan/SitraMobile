package com.sitramobile.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sitramobile.activity.LoginActivity
import com.sitramobile.modelResponse.CustomerModel
import com.sitramobile.modelResponse.LoginResponse
import java.lang.reflect.Type

object Helper {

    var PT_Id:String ?=null

    fun Context.logoutUser() {

        SessionManager(this).prefs.edit().clear().apply()
        SessionManager(this).editor.commit()
        //SessionManager(this).editor.remove("LoginStatus")
        Log.e("Status",SessionManager(this).logout().toString())
        // After logout redirect user to Loing Activity
        val i = Intent(this, LoginActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // Staring Login Activity
        this.startActivity(i)
        (this as Activity).finishAffinity()
    }

    fun Context.getUser(): LoginResponse
    {
        val data = SessionManager(this).prefs.getString("UserData", "")
        return Gson().fromJson(data, LoginResponse::class.java)
    }

    fun Context.setUser(user:LoginResponse)
    {
        val data = Gson().toJson(user, LoginResponse::class.java)
        SessionManager(this).editor.putString("UserData", data).commit()
    }

    fun Context.setUserData(user_Name:String,user_Id:String,user_role:String)
    {
        SessionManager(this).editor
            .putString(Constants.USER_NAME, user_Name)
            .putString(Constants.USER_ID, user_Id)
            .putString(Constants.USER_ROLE, user_role)
            .commit()
    }

    fun Context.setCustomerID(cust_Id:String)
    {
        SessionManager(this).editor
            .putString(Constants.CUSTOMER_ID, cust_Id)
            .commit()
    }

    fun Context.getCustomerId():String
    {
        return SessionManager(this).prefs.getString(Constants.CUSTOMER_ID, "").toString()
    }

    fun Context.getUserName():String
    {
        return SessionManager(this).prefs.getString(Constants.USER_NAME, "").toString()
    }


    fun Context.getUserId():String
    {
        return SessionManager(this).prefs.getString(Constants.USER_ID, "").toString()
    }

    fun Context.getUserRole():String
    {
        return SessionManager(this).prefs.getString(Constants.USER_ROLE, "").toString()
    }



    /*fun Context.setFormPrimaryandSecondarykeyNull()
    {
        SessionManager(this).editor
            .putString(Constants.Primarykey_Name, null)
            .putString(Constants.Secondarykey_Name, null)
            .commit()
    }

    fun Context.setFormPrimarykey(key:String)
    {
        SessionManager(this).editor
            .putString(Constants.Primarykey_Name, key)
            .commit()
    }

    fun Context.setFormSecondarykey(key:String)
    {
        SessionManager(this).editor
            .putString(Constants.Secondarykey_Name, key)
            .commit()
    }

    fun Context.getFormPrimarykey():String
    {
        return SessionManager(this).prefs.getString(Constants.Primarykey_Name, "").toString()
    }

    fun Context.getFormSecondarykey():String
    {
        return SessionManager(this).prefs.getString(Constants.Secondarykey_Name, "").toString()
    }*/



    fun Context.setCutomerlist(mShift: MutableList<CustomerModel>)
    {
        val myuser=Gson().toJson(mShift)
        SessionManager(this).editor.putString("CutomerListData", myuser).commit()
    }

    fun Context.getCustomer():List<CustomerModel>
    {
        val mCustomer = SessionManager(this).prefs.getString("CutomerListData", "")
        val listType: Type = object : TypeToken<ArrayList<CustomerModel?>?>() {}.getType()
        val yourClassList: List<CustomerModel> = Gson().fromJson(mCustomer, listType)
        return yourClassList//Gson().fromJson(user, Shift::class.java)
    }

}