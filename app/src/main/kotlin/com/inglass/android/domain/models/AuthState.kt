package com.inglass.android.domain.models

sealed class AuthState {
    object LoggedIn : AuthState()
    object LoggedOut : AuthState()
}
