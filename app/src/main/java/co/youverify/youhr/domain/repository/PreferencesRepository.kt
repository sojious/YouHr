package co.youverify.youhr.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {


    suspend  fun getUserEmail(): Flow<String>

    suspend fun saveUserEmail(userEmail:String)

    suspend  fun getUserToken(): Flow<String>

    suspend fun saveUserToken(userToken:String)

    suspend fun setFirstRun(isFirstRun:Boolean)

    suspend fun getAppFirstRunStatus(): Flow<Boolean>

    suspend  fun getUserPasscodeCreationStatus(): Flow<Boolean>

    suspend  fun setUserPasscodeCreationStatus(passcodeCreated:Boolean)

    suspend fun saveUserPassword(userPassword:String)
    fun getUserPassword(): Flow<String>

    suspend fun saveUserPasscode(userPasscode:String)
    fun getUserPasscode(): Flow<String>
}