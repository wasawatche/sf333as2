package com.example.tictactoegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel: ViewModel() {
    var state by mutableStateOf(GameState())
    var randNum = 1

    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )

    fun onAction(action: UserAction) {
        when (action) {
            UserAction.PlayerAgainButtonClicked -> {
                gameReset()
            }

            is UserAction.BoardTapped -> {
                addValueToBoard(action.cellNo)
            }
        }
    }

    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE)
            return
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNo] = BoardCellValue.CIRCLE
            state = if (checkForVictory(BoardCellValue.CIRCLE)) {
                state.copy(
                    hintText = "Player 'O' Won",
                    playerCircleCount = state.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Draw",
                    drawCount = state.drawCount + 1
                )
            }
            else {
                state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNo] = BoardCellValue.CROSS
            state = if (checkForVictory(BoardCellValue.CROSS)) {
                state.copy(
                    hintText = "Player 'X' Won",
                    playerCrossCount = state.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else {
                state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
        if (!hasBoardFull()) computerMove();
    }

    private fun computerMove() {
        if (state.currentTurn == BoardCellValue.CROSS) {
            if (canWin() != 0) {
                addValueToBoard(canWin())
            } else if (canBLock() != 0) {
                addValueToBoard(canBLock())
            } else if (middleFree()) {
                addValueToBoard(5)
            } else {
                while (boardItems[randNum] != BoardCellValue.NONE) {
                    randNum = Random.nextInt(1, 10)
                }
                addValueToBoard(randNum)
            }
        }
    }

    private fun middleFree(): Boolean {
        if (boardItems[5] == BoardCellValue.NONE) {
            return true
        }
        return false
    }

    private fun canBLock(): Int {
        when {
            // HORIZONTAL1
            boardItems[1] == BoardCellValue.CIRCLE && boardItems[2] == BoardCellValue.CIRCLE &&
                    boardItems[3] == BoardCellValue.NONE -> {
                return 3
            }
            boardItems[1] == BoardCellValue.CIRCLE && boardItems[2] == BoardCellValue.NONE &&
                    boardItems[3] == BoardCellValue.CIRCLE -> {
                return 2
            }
            boardItems[1] == BoardCellValue.NONE && boardItems[2] == BoardCellValue.CIRCLE &&
                    boardItems[3] == BoardCellValue.CIRCLE -> {
                return 1
            }
            // HORIZONTAL2
            boardItems[4] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[6] == BoardCellValue.NONE -> {
                return 6
            }
            boardItems[4] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[6] == BoardCellValue.CIRCLE -> {
                return 5
            }
            boardItems[4] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[6] == BoardCellValue.CIRCLE -> {
                return 4
            }
            // HORIZONTAL3
            boardItems[7] == BoardCellValue.CIRCLE && boardItems[8] == BoardCellValue.CIRCLE &&
                    boardItems[9] == BoardCellValue.NONE -> {
                return 9
            }
            boardItems[7] == BoardCellValue.CIRCLE && boardItems[8] == BoardCellValue.NONE &&
                    boardItems[9] == BoardCellValue.CIRCLE -> {
                return 8
            }
            boardItems[7] == BoardCellValue.NONE && boardItems[8] == BoardCellValue.CIRCLE &&
                    boardItems[9] == BoardCellValue.CIRCLE -> {
                return 7
            }
            // VERTICAL1
            boardItems[1] == BoardCellValue.CIRCLE && boardItems[4] == BoardCellValue.CIRCLE &&
                    boardItems[7] == BoardCellValue.NONE -> {
                return 7
            }
            boardItems[1] == BoardCellValue.CIRCLE && boardItems[4] == BoardCellValue.NONE &&
                    boardItems[7] == BoardCellValue.CIRCLE -> {
                return 4
            }
            boardItems[1] == BoardCellValue.NONE && boardItems[4] == BoardCellValue.CIRCLE &&
                    boardItems[7] == BoardCellValue.CIRCLE -> {
                return 1
            }
            // VERTICAL2
            boardItems[2] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[8] == BoardCellValue.NONE -> {
                return 8
            }
            boardItems[2] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[8] == BoardCellValue.CIRCLE -> {
                return 5
            }
            boardItems[2] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[8] == BoardCellValue.CIRCLE -> {
                return 2
            }
            // VERTICAL3
            boardItems[3] == BoardCellValue.CIRCLE && boardItems[6] == BoardCellValue.CIRCLE &&
                    boardItems[9] == BoardCellValue.NONE -> {
                return 9
            }
            boardItems[3] == BoardCellValue.CIRCLE && boardItems[6] == BoardCellValue.NONE &&
                    boardItems[9] == BoardCellValue.CIRCLE -> {
                return 6
            }
            boardItems[3] == BoardCellValue.NONE && boardItems[6] == BoardCellValue.CIRCLE &&
                    boardItems[9] == BoardCellValue.CIRCLE -> {
                return 3
            }
            // DIAGONAL1
            boardItems[1] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[9] == BoardCellValue.NONE -> {
                return 9
            }
            boardItems[1] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[9] == BoardCellValue.CIRCLE -> {
                return 5
            }
            boardItems[1] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[9] == BoardCellValue.CIRCLE -> {
                return 1
            }
            // DIAGONAL2
            boardItems[3] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[7] == BoardCellValue.NONE -> {
                return 7
            }
            boardItems[3] == BoardCellValue.CIRCLE && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[7] == BoardCellValue.CIRCLE -> {
                return 5
            }
            boardItems[3] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CIRCLE &&
                    boardItems[7] == BoardCellValue.CIRCLE -> {
                return 3
            }
            else -> {
                return 0
            }
        }
    }

    private fun canWin(): Int {
        when {
            // HORIZONTAL1
            boardItems[1] == BoardCellValue.CROSS && boardItems[2] == BoardCellValue.CROSS &&
                    boardItems[3] == BoardCellValue.NONE -> {
                return 3
            }
            boardItems[1] == BoardCellValue.CROSS && boardItems[2] == BoardCellValue.NONE &&
                    boardItems[3] == BoardCellValue.CROSS -> {
                return 2
            }
            boardItems[1] == BoardCellValue.NONE && boardItems[2] == BoardCellValue.CROSS &&
                    boardItems[3] == BoardCellValue.CROSS -> {
                return 1
            }
            // HORIZONTAL2
            boardItems[4] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[6] == BoardCellValue.NONE -> {
                return 6
            }
            boardItems[4] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[6] == BoardCellValue.CROSS -> {
                return 5
            }
            boardItems[4] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[6] == BoardCellValue.CROSS -> {
                return 4
            }
            // HORIZONTAL3
            boardItems[7] == BoardCellValue.CROSS && boardItems[8] == BoardCellValue.CROSS &&
                    boardItems[9] == BoardCellValue.NONE -> {
                return 9
            }
            boardItems[7] == BoardCellValue.CIRCLE && boardItems[8] == BoardCellValue.NONE &&
                    boardItems[9] == BoardCellValue.CROSS -> {
                return 8
            }
            boardItems[7] == BoardCellValue.NONE && boardItems[8] == BoardCellValue.CROSS &&
                    boardItems[9] == BoardCellValue.CROSS -> {
                return 7
            }
            // VERTICAL1
            boardItems[1] == BoardCellValue.CROSS && boardItems[4] == BoardCellValue.CROSS &&
                    boardItems[7] == BoardCellValue.NONE -> {
                return 7
            }
            boardItems[1] == BoardCellValue.CROSS && boardItems[4] == BoardCellValue.NONE &&
                    boardItems[7] == BoardCellValue.CROSS -> {
                return 4
            }
            boardItems[1] == BoardCellValue.NONE && boardItems[4] == BoardCellValue.CROSS &&
                    boardItems[7] == BoardCellValue.CROSS -> {
                return 1
            }
            // VERTICAL2
            boardItems[2] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[8] == BoardCellValue.NONE -> {
                return 8
            }
            boardItems[2] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[8] == BoardCellValue.CROSS -> {
                return 5
            }
            boardItems[2] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[8] == BoardCellValue.CROSS -> {
                return 2
            }
            // VERTICAL3
            boardItems[3] == BoardCellValue.CROSS && boardItems[6] == BoardCellValue.CROSS &&
                    boardItems[9] == BoardCellValue.NONE -> {
                return 9
            }
            boardItems[3] == BoardCellValue.CROSS && boardItems[6] == BoardCellValue.NONE &&
                    boardItems[9] == BoardCellValue.CROSS -> {
                return 6
            }
            boardItems[3] == BoardCellValue.NONE && boardItems[6] == BoardCellValue.CROSS &&
                    boardItems[9] == BoardCellValue.CROSS -> {
                return 3
            }
            // DIAGONAL1
            boardItems[1] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[9] == BoardCellValue.NONE -> {
                return 9
            }
            boardItems[1] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[9] == BoardCellValue.CROSS -> {
                return 5
            }
            boardItems[1] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[9] == BoardCellValue.CROSS -> {
                return 1
            }
            // DIAGONAL2
            boardItems[3] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[7] == BoardCellValue.NONE -> {
                return 7
            }
            boardItems[3] == BoardCellValue.CROSS && boardItems[5] == BoardCellValue.NONE &&
                    boardItems[7] == BoardCellValue.CROSS -> {
                return 5
            }
            boardItems[3] == BoardCellValue.NONE && boardItems[5] == BoardCellValue.CROSS &&
                    boardItems[7] == BoardCellValue.CROSS -> {
                return 3
            }
            else -> {
                return 0
            }
        }
    }

    public fun hasBoardFull(): Boolean {
            return !boardItems.containsValue(BoardCellValue.NONE)
    }

    private fun gameReset() {
        boardItems.forEach {(i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        if (hasBoardFull()) {
            state = state.copy(
                hintText = "Player 'O' turn",
                currentTurn = BoardCellValue.CIRCLE,
                victoryType = VictoryType.NONE,
                hasWon = false
            )
        } else {
            state = state.copy(
                hintText = "Player 'O' turn",
                currentTurn = BoardCellValue.CIRCLE,
                drawCount = state.drawCount + 1,
                victoryType = VictoryType.NONE,
                hasWon = false
            )
        }
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue &&
                    boardItems[3] == boardValue -> {
                        state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
                    }
            boardItems[4] == boardValue && boardItems[5] == boardValue &&
                    boardItems[6] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItems[7] == boardValue && boardItems[8] == boardValue &&
                    boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[4] == boardValue &&
                    boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItems[2] == boardValue && boardItems[5] == boardValue &&
                    boardItems[8] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItems[3] == boardValue && boardItems[6] == boardValue &&
                    boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[5] == boardValue &&
                    boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItems[3] == boardValue && boardItems[5] == boardValue &&
                    boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
           else -> {
                return false
            }
        }
    }
}