package com.sitramobile.modelResponse

class FormlookupResponse : ArrayList<FormlookupResponseItem>()

data class FormlookupResponseItem(
    val Combo_list: String,
    val DispName: String,
    val Field_Name: String,
    val ScreenNo: String
)