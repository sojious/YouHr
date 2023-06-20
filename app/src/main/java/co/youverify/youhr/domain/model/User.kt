package co.youverify.youhr.domain.model

import android.graphics.Bitmap


data class User(
    val role: String,
    val jobRole:String,
    val status: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName:String,
    val phoneNumber: String,
    val password: String,
    val passcode: String,
    val address: String,
    val dob: String,
    val gender: String,
    val nextOfKin: String,
    val nextOfKinContact: String,
    val displayPictureUrl: String,
    val displayPictureBitmap: Bitmap?=null,
    val nextOfKinNumber: String,
    val id: String
)