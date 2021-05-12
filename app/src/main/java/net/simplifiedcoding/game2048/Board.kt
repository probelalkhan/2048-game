package net.simplifiedcoding.game2048

import android.graphics.Color
import android.graphics.Point
import net.simplifiedcoding.game2048.databinding.CellBinding

class Board {

    var board = Array(4) { IntArray(4) { 0 } }

    init {
        spawnRandomTwo()
        spawnRandomTwo()
    }

    fun spawnRandomTwo() {
        val emptyCells = mutableListOf<Point>()
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == 0) {
                    emptyCells.add(Point(i, j))
                }
            }
        }
        if (emptyCells.isNotEmpty()) {
            val randomCell = emptyCells[(0 until emptyCells.size).random()]
            board[randomCell.x][randomCell.y] = 2
        }
    }

    fun hasPlayerWon(board: Array<IntArray>): Boolean {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == 2048) {
                    return true
                }
            }
        }
        return false
    }

    fun isGameOver(board: Array<IntArray>): Boolean {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == 0) {
                    return false
                }
            }
        }
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == board[i + 1][j] || board[i][j] == board[i][j + 1]) {
                    return false
                }
            }
        }

        for (j in 0..2) {
            if (board[3][j] == board[3][j + 1])
                return false
        }

        for (i in 0..2) {
            if (board[i][3] == board[i + 1][3])
                return false
        }

        return true
    }

    private fun compressBoard(board: Array<IntArray>): Pair<Array<IntArray>, Boolean> {
        var changed = false
        val newBoard = Array(4) { IntArray(4) { 0 } }

        for (i in 0..3) {
            var pos = 0
            for (j in 0..3) {
                if (board[i][j] != 0) {
                    newBoard[i][pos] = board[i][j]
                    if (j != pos)
                        changed = true
                    pos++
                }
            }
        }
        return Pair(newBoard, changed)
    }

    private fun mergeBoard(board: Array<IntArray>): Pair<Array<IntArray>, Boolean> {
        var changed = false
        for (i in 0..3) {
            for (j in 0..2) {
                if (board[i][j] == board[i][j + 1] && board[i][j] != 0) {
                    board[i][j] = board[i][j] * 2
                    board[i][j + 1] = 0
                    changed = true
                }
            }
        }
        return Pair(board, changed)
    }

    private fun reverseBoard(board: Array<IntArray>): Array<IntArray> {
        val newBoard = Array(4) { IntArray(4) { 0 } }
        for (i in 0..3) {
            for (j in 0..3) {
                newBoard[i][j] = board[i][3 - j]
            }
        }
        return newBoard
    }

    private fun transposeBoard(board: Array<IntArray>): Array<IntArray> {
        val newBoard = Array(4) { IntArray(4) { 0 } }
        for (i in 0..3) {
            for (j in 0..3) {
                newBoard[i][j] = board[j][i]
            }
        }
        return newBoard
    }

    fun moveLeft(board: Array<IntArray>): Pair<Array<IntArray>, Boolean> {
        val (newBoard1, isChanged1) = compressBoard(board)
        val (newBoard2, isChanged2) = mergeBoard(newBoard1)
        val isChanged = isChanged1 || isChanged2
        val (newBoard3, _) = compressBoard(newBoard2)
        return Pair(newBoard3, isChanged)
    }

    fun moveRight(board: Array<IntArray>): Pair<Array<IntArray>, Boolean> {
        var newBoard = reverseBoard(board)
        val (newBoard1, isChanged) = moveLeft(newBoard)
        newBoard = reverseBoard(newBoard1)
        return Pair(newBoard, isChanged)
    }

    fun moveUp(board: Array<IntArray>): Pair<Array<IntArray>, Boolean> {
        var newBoard = transposeBoard(board)
        val (newBoard1, isChanged) = moveLeft(newBoard)
        newBoard = transposeBoard(newBoard1)
        return Pair(newBoard, isChanged)
    }

    fun moveDown(board: Array<IntArray>): Pair<Array<IntArray>, Boolean> {
        var newBoard = transposeBoard(board)
        val (newBoard1, isChanged) = moveRight(newBoard)
        newBoard = transposeBoard(newBoard1)
        return Pair(newBoard, isChanged)
    }
}