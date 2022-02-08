package com.sitramobile.modelResponse

import com.google.gson.JsonObject


class FormSublist : ArrayList<FormSublistItem>()

data class FormSublistItem(
    val PT_Id: String,
    val PT_Sub_key: String?=null
)


class ArrFormSublist : ArrayList<JsonObject>()