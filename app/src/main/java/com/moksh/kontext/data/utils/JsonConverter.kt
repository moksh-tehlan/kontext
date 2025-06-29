package com.moksh.kontext.data.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object JsonConverter {

    inline fun <reified T> fromJson(json: String): T? {
        return try {
            Gson().fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    fun <T> toJson(obj: T): String {
        return Gson().toJson(obj)
    }
}
