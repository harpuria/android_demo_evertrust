package com.yhh.library_demo.data


import com.google.gson.annotations.SerializedName

data class SeoulPublicLibraryInfo(
    @SerializedName("list_total_count")
    val listTotalCount: Int,
    @SerializedName("RESULT")
    val rESULT: RESULT,
    @SerializedName("row")
    val row: List<Row>
)