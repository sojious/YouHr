package co.youverify.youhr.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val USER_EMAIL= stringPreferencesKey(name = "user_email")
    val FIRST_RUN= booleanPreferencesKey(name = "first_run")
    val CREATED_CODE= booleanPreferencesKey(name = "created_code")
    val USER_TOKEN= stringPreferencesKey(name = "user_token")
}