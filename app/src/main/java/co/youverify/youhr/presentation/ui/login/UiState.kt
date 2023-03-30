package co.youverify.youhr.presentation.ui.login

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.AuthResponse

data class UiState (
    val loading:Boolean=false,
    val authenticated:Boolean=false,
    val authenticationError:String=""
)
