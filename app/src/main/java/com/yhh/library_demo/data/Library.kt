package com.yhh.library_demo.data


import com.google.gson.annotations.SerializedName

data class Library(
    @SerializedName("SeoulPublicLibraryInfo")
    val seoulPublicLibraryInfo: SeoulPublicLibraryInfo
)