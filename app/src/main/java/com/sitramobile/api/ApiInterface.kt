package com.sitramobile.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sitramobile.modelRequest.*
import com.sitramobile.modelResponse.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @POST("user_authentication")
    fun login(@Body data: LoginRequest): Call<LoginResponse>

    @FormUrlEncoded
    @POST("select_customer")
    fun customerList(@Field("username") username: String?=""): Call<List<CustomerModel>>//Call<JsonArray?>?

    @POST("select_screen")
    fun categoryMenuList(@Body id: SelectScreenRequest?): Call<JsonArray?>?

    @POST("select_fields")
    fun selectFields(@Body id: ScreenFormRequest?): Call<JsonArray?>?

    @POST("get_observation_keyfield_data")
    fun getItemListData(@Body id: ObservationDataList): Call<FormSublist>

    //new 28.06.2021
    @POST("get_observation_keyfield_data")
    fun getItemListData2(@Body id: ObservationDataList): Call<JsonArray>

    @POST("get_observation_data")
    fun getFieldData(@Body data: JsonObject?): Call<JsonArray?>?

    //@PUT("update_observation_data")
    @POST("update_observation_data")
    fun updateFieldData(@Body data: JsonObject?): Call<Boolean?>?

    @POST("get_master_data")
    fun getMasterData(@Body data:JsonObject?): Call<JsonArray?>?
    //fun getMasterData(@Body id:MasterDataRequest): Call<JsonArray?>?

    @POST("get_master_field_description ")
    fun getMasterFieldData(@Body id: ScreenNoRequest): Call<JsonArray?>?

    @POST("update_master_data")
    fun updateMasterFieldData(@Body data: JsonObject?): Call<Boolean?>?

    @POST("get_obs_field_lookup")
    fun getFormFieldLookup(@Body id: ScreenNoRequest): Call<FormlookupResponse>

    @POST("get_master_field_lookup")
    fun getMasterFieldLookup(@Body id: ScreenNoRequest): Call<MasterlookupResponse?>


    /**
    get_master_data
    update_master_data
    get_obs_field_description
    get_master_field_description
     **/
}


/*
public interface ApiInterface {

    @POST("user_authentication")
    Call<LoginResponse> login(@Body LoginRequest data);

    @FormUrlEncoded
    @POST("select_customer")
    Call<JsonArray> customerList(@Field("username") String username);

    @POST("select_screen")
    Call<JsonArray> categoryMenuList(@Body SelectScreenRequest id);

    @POST("select_fields")
    Call<JsonArray> selectFields(@Body ScreenFormRequest id);

    @POST("get_observation_data")
    Call<JsonArray> getFieldData(@Body ObservationDataRequest id);

    //@POST("update_observation_data")
    @PUT("update_observation_data")
    Call<Boolean> updateFieldData(@Body JsonObject data);


}
 */