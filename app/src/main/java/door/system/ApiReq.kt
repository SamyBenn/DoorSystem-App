import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class ApiReq {
    companion object {
        private val client = OkHttpClient()

        fun post(url: String, json: String, callback: (String?, String?) -> Unit) {
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, json)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(null, e.message)
                    Log.d("ApiReq", "Error: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body
                        if (responseBody != null) {
                            val responseString = responseBody.string()
                            Log.d("ApiReq", "Response: $responseString")
                            callback(responseString, null)
                        } else {
                            callback("", "Response body is null")
                        }
                    } else {
                        callback("", "Response not successful")
                    }
                }
            })
        }
    }
}
