package co.youverify.youhr.data.repository.profile

import co.youverify.youhr.data.local.DbUser

interface ProfileLocalDataSource {
    suspend fun getUser(): DbUser
    suspend fun saveUser(user: DbUser)
}