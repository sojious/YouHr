package co.youverify.youhr.core.util

sealed class Result<T:Any>{


    class Success<T : Any>(val data: T):Result<T>()
    class Error<T : Any>(val code: Int, val message: String?):Result<T>()
    class Exception<T:Any>(val e:Throwable, val genericMessage:String):Result<T>()


}




