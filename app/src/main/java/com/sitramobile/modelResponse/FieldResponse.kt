package com.sitramobile.modelResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*class FieldResponse {
    @SerializedName("ScreenNo")
    @Expose
    var screenNo: String? = null

    @SerializedName("Field_Name")
    @Expose
    var fieldName: String? = null

    @SerializedName("DispName")
    @Expose
    var dispName: String? = null

    @SerializedName("DataType")
    @Expose
    var dataType: String? = null

    @SerializedName("Mandatory")
    @Expose
    var mandatory: String? = null
    var fieldValue = ""
}*/

data class FieldResponse(
    var ScreenNo: String,
    var Field_Name:String,
    var DispName: String,
    var DataType: String,
    var Mandatory: String,
    var fieldValue:String ?= ""
)