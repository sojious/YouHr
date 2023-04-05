package co.youverify.youhr.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor:Interceptor {

    private var token:String=""

    fun setToken(token:String){
        this.token=token
    }
    fun getToken()=token


    override fun intercept(chain: Interceptor.Chain): Response {

        var request=chain.request()

        if(request.header("No-Authentication")==null){
            if (token.isNotEmpty())
                request=request.newBuilder().addHeader("Authorization","Bearer $token").build()
        }

        return chain.proceed(request)
    }
}