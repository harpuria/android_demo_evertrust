package com.yhh.library_demo

import com.yhh.library_demo.data.Library
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class SeoulOpenApi {
    companion object{
        val BASE_URL = "http://openapi.seoul.go.kr:8088/"
        val API_KEY = "715476714c68617234356a68435061"
    }
}

interface SeoulOpenApiService {
    // 도서관 정보 가져오기
    @GET("{api_key}/json/SeoulPublicLibraryInfo/1/200")
    fun getLibraryinfo(@Path("api_key") key:String):Call<Library>
}