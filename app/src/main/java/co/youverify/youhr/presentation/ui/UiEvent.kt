package co.youverify.youhr.presentation.ui

sealed class UiEvent{
    data class ShowSnackBar(val message:String) : UiEvent()
    data class ShowToast(val message: String) :UiEvent()
}