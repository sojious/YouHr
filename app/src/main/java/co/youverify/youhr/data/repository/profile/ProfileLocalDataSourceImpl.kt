package co.youverify.youhr.data.repository.profile

import co.youverify.youhr.data.local.DbUser
import co.youverify.youhr.data.local.YouHrDatabase
import javax.inject.Inject

class ProfileLocalDataSourceImpl  @Inject constructor(private val youHrDatabase: YouHrDatabase):ProfileLocalDataSource {
    override suspend fun getUser(): DbUser {
        return youHrDatabase.userDao().getUser()
    }

    override suspend fun saveUser(user: DbUser) {
        youHrDatabase.userDao().saveUser(user)
    }
}