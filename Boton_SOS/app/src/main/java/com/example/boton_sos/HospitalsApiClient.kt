package com.example.boton_sos

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class HospitalsApiClient(private val context: Context) {

    private val client = OkHttpClient()

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

   fun fetchNearbyHospitals(lat: Double, lon: Double, radius: Int = 5000, onResult: (List<String>?) -> Unit) {
    if (!isInternetAvailable()) {
        onResult(null)
        return
    }

    val overpassUrl = "https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=hospital](around:$radius,$lat,$lon);out;"
    val request = Request.Builder()
        .url(overpassUrl)
        .build()

    Thread {
        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonResponse = response.body?.string()
                val hospitalNames = processHospitalsResponse(jsonResponse)
                Log.d("OverpassAPI", jsonResponse ?: "No response")
                onResult(hospitalNames)
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

private fun processHospitalsResponse(jsonResponse: String?): List<String> {
    val hospitals = mutableListOf<String>()
    if (jsonResponse != null) {
        val jsonArray = JSONObject(jsonResponse).getJSONArray("elements")
        for (i in 0 until jsonArray.length()) {
            val element = jsonArray.getJSONObject(i)
            val tags = element.getJSONObject("tags")
            val name = tags.optString("name", "Hospital sin nombre")
            hospitals.add(name)
        }
    }
    return hospitals.take(5) 
}
