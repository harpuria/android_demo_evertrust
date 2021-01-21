package com.yhh.library_demo

import com.yhh.library_demo.data.User

class Singleton private constructor(){
    companion object{
        @Volatile private var instance: Singleton? = null
        @JvmStatic fun getInstance(): Singleton =
            instance ?: synchronized(this) {
                instance ?: Singleton().also {
                    instance = it
                }
            }

        lateinit var curUser:User
    }
}