package co.youverify.youhr.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import co.youverify.youhr.core.util.PreferenceKeys
import co.youverify.youhr.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(private val preferencesDataStore: DataStore<Preferences>) :PreferencesRepository{

    override suspend fun getUserEmail(): Flow<String> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.USER_EMAIL]?:""
            }
    }

    override suspend fun saveUserEmail(userEmail:String) {
        preferencesDataStore.edit { mutablePreferences->
            mutablePreferences[PreferenceKeys.USER_EMAIL] = userEmail
        }
    }

    override suspend fun getUserToken(): Flow<String> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.USER_TOKEN]?:""
            }
    }

    override suspend fun saveUserToken(userToken: String) {
        preferencesDataStore.edit { mutablePreferences->
            mutablePreferences[PreferenceKeys.USER_TOKEN] = userToken
        }
    }

    override suspend fun setFirstRun(isFirstRun: Boolean) {
        preferencesDataStore.edit {mutablePreferences ->
            mutablePreferences[PreferenceKeys.FIRST_RUN] = isFirstRun
        }
    }

    override suspend fun getAppFirstRunStatus(): Flow<Boolean> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.FIRST_RUN]?: true
            }
    }

    override suspend fun getUserPasscodeCreationStatus(): Flow<Boolean> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.CREATED_CODE]?:false
            }
    }

    override suspend fun setUserPasscodeCreationStatus(passcodeCreated: Boolean) {
        preferencesDataStore.edit {mutablePreferences ->
            mutablePreferences[PreferenceKeys.CREATED_CODE] = passcodeCreated
        }
    }

    override suspend fun getLogOutStatus(): Flow<Boolean> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.LOGGED_OUT]?:false
            }
    }

    override suspend fun setLogOutStatus(loggedOut: Boolean) {
        preferencesDataStore.edit {mutablePreferences ->
            mutablePreferences[PreferenceKeys.LOGGED_OUT] = loggedOut
        }
    }

    override suspend fun saveUserPassword(userPassword: String) {
        preferencesDataStore.edit { mutablePreferences->
            mutablePreferences[PreferenceKeys.USER_PASSWORD] = userPassword
        }
    }

    override fun getUserPassword(): Flow<String> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.USER_PASSWORD]?:""
            }
    }

    override suspend fun saveUserPasscode(userPasscode: String) {
        preferencesDataStore.edit { mutablePreferences->
            mutablePreferences[PreferenceKeys.USER_PASSCODE] = userPasscode
        }
    }

    override fun getUserPasscode(): Flow<String> {
        return  preferencesDataStore.data
            .catch {exception->
                //if (exception is IOException) emptyPreferences() else throw exception
                throw exception
            }.map {preferences->
                preferences[PreferenceKeys.USER_PASSCODE]?:""
            }
    }
}