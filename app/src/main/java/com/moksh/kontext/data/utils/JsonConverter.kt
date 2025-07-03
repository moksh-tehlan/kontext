package com.moksh.kontext.data.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object JsonConverter {

    inline fun <reified T> fromJson(json: String): T? {
        return try {
            // Use a more R8-safe approach
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            gson.fromJson<T>(json, type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> toJson(obj: T): String {
        return Gson().toJson(obj)
    }
}
