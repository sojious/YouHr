package co.youverify.youhr.presentation.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val navigator: Navigator):ViewModel() {


    var currentUser: User? by mutableStateOf(null)
        private set

    fun onSettingsItemClicked(index: Int) {}
    fun onProfilePicClicked() {
        navigator.navigate(Profile.route)
    }

    fun updateCurrentUser(user: User?) {
        currentUser=user
    }
}