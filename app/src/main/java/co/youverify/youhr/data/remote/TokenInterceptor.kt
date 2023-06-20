package co.youverify.youhr.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor:Interceptor {

    private var token:String=""

    fun setToken(token:String){
        this.token = token
    }
    fun getToken() = token


    override fun intercept(chain: Interceptor.Chain): Response {

        var request=chain.request()

        // if the request doesn't have a "No-Authentication" header attached to it, it means that it requires Authorization
        //Attach the bearer token Authorization header to it
        if(request.header("No-Authentication") == null){
            if (token.isNotEmpty()){
                request = request.newBuilder()
                    .addHeader("Authorization","Bearer $token")
                    .build()
            }

        }

        return chain.proceed(request)
    }
}