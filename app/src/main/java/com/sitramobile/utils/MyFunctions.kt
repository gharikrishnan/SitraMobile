package com.sitramobile.utils

import android.content.Context
import com.google.gson.JsonArray
import com.sitramobile.modelResponse.LoginResponse
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.sitramobile.modelResponse.CustomerModel
import com.sitramobile.modelResponse.CategoryModel
import com.sitramobile.modelResponse.FieldResponse
import com.google.gson.JsonObject

object MyFunctions
{
    private const val MY_PREFS_NAME = "MyPrefsSitra"

    // Shared preference save & get string values

    fun setStringSharedPref(context: Context, key_name: String?, value: String?)
    {
          val editor =  context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit()
        editor.putString(key_name, value)
        editor.apply()
    }

    fun setIntSharedPref(context: Context, key_name: String?, value: Int)
    {
        val editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putInt(key_name, value)
        editor.apply()
    }

    // Shared preference save & get integer values
    fun getStringSharedPref(context: Context, key_name: String?): String?
    {
        val prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key_name, "")
    }

    fun getIntSharedPref(context: Context, key_name: String?, value: Int): Int
    {
        val prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(key_name, 0)
    }

    //Convert Json array response to model class array Login Response
    fun JsonToModelLogin(response: JsonArray?): List<LoginResponse> {
        val contacts: List<LoginResponse>
        val listType = object : TypeToken<List<LoginResponse?>?>() {}.type
        contacts = Gson().fromJson(response, listType)
        return contacts
    }

    //Convert Json array response to model class array Customer list Response
    fun JsonToModelCustomerList(response: JsonArray?): List<CustomerModel> {
        val contacts: List<CustomerModel>
        val listType = object : TypeToken<List<CustomerModel?>?>() {}.type
        contacts = Gson().fromJson(response, listType)
        return contacts
    }

    //Convert Json array response to model class array Category list Response
    fun JsonToModelCategoryList(response: JsonArray?): List<CategoryModel> {
        val contacts: List<CategoryModel>
        val listType = object : TypeToken<List<CategoryModel?>?>() {}.type
        contacts = Gson().fromJson(response, listType)
        return contacts
    }

    //Convert Json array response to model class array FieldResponse list Response
    fun JsonToModelField(response: JsonArray?): List<FieldResponse>? {
        val contacts: List<FieldResponse>
        val listType = object : TypeToken<List<FieldResponse?>?>() {}.type
        contacts = Gson().fromJson(response, listType)
        return contacts
    }

    //Convert Json array response to Json object Response
    fun JsonArrayToJsonObject(response: JsonArray?): JsonObject? {
        val contacts: List<JsonObject>
        val listType = object : TypeToken<List<JsonObject?>?>() {}.type
        contacts = Gson().fromJson(response, listType)
        return if (!contacts.isEmpty()) contacts[0] else null
    }
}
/*
package com.sitramobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sitramobile.modelResponse.CategoryModel;
import com.sitramobile.modelResponse.CustomerModel;
import com.sitramobile.modelResponse.FieldResponse;
import com.sitramobile.modelResponse.LoginResponse;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyFunctions {

    public static final String MY_PREFS_NAME = "MyPrefsSitra";

    // Shared preference save & get string values
    public static void setStringSharedPref(Context context, String key_name, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key_name, value);
        editor.apply();
    }

    public static void setIntSharedPref(Context context, String key_name, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(key_name, value);
        editor.apply();
    }

    // Shared preference save & get integer values
    public static String getStringSharedPref(Context context, String key_name) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key_name, "");
    }

    public static int getIntSharedPref(Context context, String key_name, int value) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(key_name, 0);
    }

    //Convert Json array response to model class array Login Response
    public static List<LoginResponse> JsonToModelLogin(JsonArray response) {
        List<LoginResponse> contacts;
        Type listType = new TypeToken<List<LoginResponse>>() {}.getType();
        contacts = new Gson().fromJson(response, listType);
        return contacts;
    }

    //Convert Json array response to model class array Customer list Response
    public static List<CustomerModel> JsonToModelCustomerList(JsonArray response) {
        List<CustomerModel> contacts;
        Type listType = new TypeToken<List<CustomerModel>>() {
        }.getType();
        contacts = new Gson().fromJson(response, listType);
        return contacts;
    }

    //Convert Json array response to model class array Category list Response
    public static List<CategoryModel> JsonToModelCategoryList(JsonArray response) {
        List<CategoryModel> contacts;
        Type listType = new TypeToken<List<CategoryModel>>() {}.getType();
        contacts = new Gson().fromJson(response, listType);
        return contacts;
    }

    //Convert Json array response to model class array FieldResponse list Response
    public static List<FieldResponse> JsonToModelField(JsonArray response) {
        List<FieldResponse> contacts;
        Type listType = new TypeToken<List<FieldResponse>>() {}.getType();
        contacts = new Gson().fromJson(response, listType);
        return contacts;
    }

    //Convert Json array response to Json object Response
    public static JsonObject JsonArrayToJsonObject(JsonArray response) {
        List<JsonObject> contacts;
        Type listType = new TypeToken<List<JsonObject>>() {}.getType();
        contacts = new Gson().fromJson(response, listType);
        if (!contacts.isEmpty())
            return contacts.get(0);
        else
            return null;
    }

}

 */