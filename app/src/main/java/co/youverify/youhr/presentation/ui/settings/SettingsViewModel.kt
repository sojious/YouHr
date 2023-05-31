package co.youverify.youhr.presentation.ui.settings

import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val navigator: Navigator):ViewModel() {


    var currentUser: User= User(
        name = "Edit",
        email = "Edith@youverify.co",
        role = "Project Manager",
        dob = "12/12/1997",
        phone = "08037582010",
        gender = "Female",
        address = "No 12, Akintola str Yaba Lagos",
        nextOfKin = "Yvonne Johnson",
        nextOfKinPhoneNumber = "08149502340"
    )
        private set

    fun onSettingsItemClicked(index: Int) {}
    fun onProfilePicClicked() {
        navigator.navigate(Profile.route)
    }
}