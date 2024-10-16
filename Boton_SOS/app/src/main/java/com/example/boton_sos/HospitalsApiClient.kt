package com.example.boton_sos

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import android.util.Log

class HospitalsApiClient {

    private val client = OkHttpClient()

    fun fetchNearbyHospitals(lat: Double, lon: Double, radius: Int = 5000, onResult: (String?) -> Unit) {

        val overpassUrl = "https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=hospital](around:$radius,$lat,$lon);out;"


        val request = Request.Builder()
            .url(overpassUrl)
            .build()

        Thread {
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    Log.d("OverpassAPI", jsonResponse ?: "No response")
                    onResult(jsonResponse)
                } else {
                    Log.e("OverpassAPI", "Request failed with code: ${response.code}")
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e("OverpassAPI", "Error: ${e.message}")
                onResult(null)
            }
        }.start()
    }
}
