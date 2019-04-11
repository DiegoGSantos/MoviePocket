package com.diego.mvvmwithscreenstates

import java.io.File

class JsonUtils {
    companion object {
        fun getJson(path : String) : String {
            // Load the JSON response
            val uri = this.javaClass.classLoader.getResource(path)
            val file = File(uri.path)
            return String(file.readBytes())
        }
    }
}