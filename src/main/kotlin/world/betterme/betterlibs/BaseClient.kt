package world.betterme.betterlibs

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

interface BaseClient : Runnable {

    companion object {

        val client = OkHttpClient()
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        const val AUTHORIZATION = "Authorization"
        val requestBuilder: Request.Builder
            get() {
                return Request.Builder()
                    .addHeader("User-Agent", "betterlibs-gradle-plugin")
                    .addHeader("Content-Type", "application/json")
            }
    }

    fun postRequest(
        json: String,
        url: String,
        headerName: String,
        headerValue: String
    ): Pair<Response?, Int> {
        val requestBody = json.toRequestBody(JSON)
        val request = requestBuilder.addHeader(headerName, headerValue)
            .url(url)
            .post(requestBody)
            .build()
        var response: Response? = null
        val status: Int

        try {
            response = client.newCall(request).execute()
            status = response.code
        } finally {
            response?.close()
        }
        return Pair(response, status)
    }
}
