package co.youverify.youhr.presentation.ui.settings.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val navigator: Navigator):ViewModel() {


    var currentUser: User? by mutableStateOf(null)
    private set

    fun updateProfile() {}
    fun updateCurrentUser(user: User?) {
        currentUser=user
    }
}