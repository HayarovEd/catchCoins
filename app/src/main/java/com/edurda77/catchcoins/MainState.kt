package com.edurda77.catchcoins

sealed class MainState {
    class Success (
        val url: String
    ) : MainState()

    object Mock : MainState()
    object NoInternet : MainState()
    object Loading : MainState()
}