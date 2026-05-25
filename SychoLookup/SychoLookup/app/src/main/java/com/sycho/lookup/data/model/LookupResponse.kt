package com.sycho.lookup.data.model

import com.google.gson.annotations.SerializedName

data class LookupResponse(
    @SerializedName("success")       val success: Boolean,
    @SerializedName("query")         val query: String,
    @SerializedName("type")          val type: String,
    @SerializedName("results_count") val resultsCount: Int,
    @SerializedName("results")       val results: List<LookupResult>,
    @SerializedName("developer")     val developer: String?,
    @SerializedName("message")       val message: String?,
    @SerializedName("timestamp")     val timestamp: String?,
    @SerializedName("source")        val source: String?
)

data class LookupResult(
    @SerializedName("mobile")      val mobile: String?,
    @SerializedName("name")        val name: String?,
    @SerializedName("cnic")        val cnic: String?,
    @SerializedName("address")     val address: String?,
    @SerializedName("table_index") val tableIndex: Int?,
    @SerializedName("row_index")   val rowIndex: Int?
)
