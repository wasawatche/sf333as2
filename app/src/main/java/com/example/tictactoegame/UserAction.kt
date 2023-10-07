package com.example.tictactoegame

sealed class UserAction {
    object PlayerAgainButtonClicked: UserAction()
    data class BoardTapped(val cellNo: Int): UserAction()
}
