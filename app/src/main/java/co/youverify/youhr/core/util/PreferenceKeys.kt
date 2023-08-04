package co.youverify.youhr.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val USER_PASSCODE = stringPreferencesKey(name = "user_passcode")
    val USER_PASSWORD = stringPreferencesKey(name = "user_password")
    val USER_EMAIL= stringPreferencesKey(name = "user_email")
    val FIRST_RUN= booleanPreferencesKey(name = "first_run")
    val CREATED_CODE= booleanPreferencesKey(name = "created_code")
    val USER_TOKEN= stringPreferencesKey(name = "user_token")
    val LOGGED_OUT= booleanPreferencesKey(name = "logged_out")
}
