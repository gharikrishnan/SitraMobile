package com.sitramobile.modelResponse

class MasterlookupResponse : ArrayList<MasterlookupResponseItem>()

data class MasterlookupResponseItem(
    val Combo_list: String,
    val DispName: String,
    val Field_Name: String,
    val ScreenNo: String
)