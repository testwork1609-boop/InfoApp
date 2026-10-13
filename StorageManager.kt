package com.infoapp.app

import android.content.Context
import com.google.gson.Gson
import java.io.File

data class User(
    val login: String,
    val haslo: String,
    val rola: String
)

data class Ogloszenie(
    val id: String,
    val tytul: String,
    val tresc: String,
    val autor: String,
    val wazne: Boolean = false,
    val data: String = ""
)

data class AppData(
    val users: MutableList<User> = mutableListOf(),
    val ogloszenia: MutableList<Ogloszenie> = mutableListOf()
)

class StorageManager(private val context: Context) {
    private val gson = Gson()
    private val file: File = File(context.filesDir, "infoapp_data.json")

    fun load(): AppData {
        if (!file.exists()) {
            return try {
                val json = context.assets.open("seed_data.json")
                    .bufferedReader().use { it.readText() }
                val data = gson.fromJson(json, AppData::class.java) ?: AppData()
                save(data)
                data
            } catch (e: Exception) {
                AppData()
            }
        }
        return try {
            val json = file.readText()
            gson.fromJson(json, AppData::class.java) ?: AppData()
        } catch (e: Exception) {
            AppData()
        }
    }

    fun save(data: AppData) {
        try {
            file.writeText(gson.toJson(data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
