package com.infoapp.app

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

data class RemoteResponse(
    val version: Int = 1,
    val updated_at: String = "",
    val ogloszenia: List<Ogloszenie> = emptyList()
)

object ApiManager {
    private const val URL = "https://monitor.host171356.xce.pl/ogloszenia.json"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun fetchOgloszenia(): Result<List<Ogloszenie>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(URL).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP ${response.code}"))
            }
            val body = response.body?.string() ?: return@withContext Result.failure(Exception("Puste body"))
            val remote = gson.fromJson(body, RemoteResponse::class.java)
            Result.success(remote.ogloszenia)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
