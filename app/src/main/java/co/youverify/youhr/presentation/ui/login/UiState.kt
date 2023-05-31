package co.youverify.youhr.presentation.ui.login



data class UiState (
    val loading:Boolean=false,
    val authenticated:Boolean=false,
    val authenticationError:String=""
)
