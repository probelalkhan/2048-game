package net.simplifiedcoding.game2048

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import net.simplifiedcoding.game2048.databinding.ActivityMainBinding
import net.simplifiedcoding.game2048.databinding.CellBinding

class MainActivity : AppCompatActivity() {

    private val gameBoard = Array(4) { arrayOfNulls<CellBinding>(4) }
    private var board = Board()
    private lateinit var binding: ActivityMainBinding
    private var fullWidth: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDisplayMetrics().also { fullWidth = it.x }
        loadBoard()
        mapBoardToUi()

        binding.layoutBoard.setOnTouchListener(touchListener)

        binding.buttonRestart.setOnClickListener {
            board = Board()
            mapBoardToUi()
        }
    }

    private val touchListener = TouchListener {
        when (it) {
            Direction.LEFT -> {
                val (newBoard, isChanged) = board.moveLeft(board.board)
                board.board = newBoard
            }
            Direction.RIGHT -> {
                val (newBoard, isChanged) = board.moveRight(board.board)
                board.board = newBoard
            }
            Direction.UP -> {
                val (newBoard, isChanged) = board.moveUp(board.board)
                board.board = newBoard
            }
            Direction.DOWN -> {
                val (newBoard, isChanged) = board.moveDown(board.board)
                board.board = newBoard
            }
        }

        when {
            board.hasPlayerWon(board.board) -> {
                binding.imageViewResult.setBackgroundResource(R.drawable.you_won)
            }
            board.isGameOver(board.board) -> {
                binding.imageViewResult.setBackgroundResource(R.drawable.game_over)
            }
            else -> {
                board.spawnRandomTwo()
                mapBoardToUi()
            }
        }
    }

    private fun mapBoardToUi() = with(board) {
        for (i in board.indices) {
            for (j in board.indices) {
                if (board[i][j] != 0) {
                    gameBoard[i][j]?.textViewLabel?.text = board[i][j].toString()
                } else {
                    gameBoard[i][j]?.textViewLabel?.text = ""
                }
                changeCellBackground(gameBoard[i][j], board[i][j])
            }
        }
    }

    private fun loadBoard() {

        val cellSize =
            if (fullWidth <= 0) DEFAULT_CELL_SIZE else (fullWidth - DEFAULT_CELL_MARGIN * 20) / 4

        for (i in gameBoard.indices) {
            for (j in gameBoard.indices) {
                gameBoard[i][j] = CellBinding.inflate(layoutInflater)
                gameBoard[i][j]?.root?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = cellSize
                    height = cellSize
                    bottomMargin = DEFAULT_CELL_MARGIN
                    topMargin = DEFAULT_CELL_MARGIN
                    leftMargin = DEFAULT_CELL_MARGIN
                    rightMargin = DEFAULT_CELL_MARGIN
                }
                binding.layoutBoard.addView(gameBoard[i][j]?.root)
            }
        }
    }

    private fun changeCellBackground(cellBinding: CellBinding?, score: Int) {
        when (score) {
            4 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#25BA90"))
            8 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#C67FBB"))
            16 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#C6814D"))
            32 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#A990B1"))
            64 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#D660F6"))
            128 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#727CAA"))
            256 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#B8435B"))
            512 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#7BEC93"))
            1024 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#B6A345"))
            2048 -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#25BA90"))
            else -> cellBinding?.cellBackground?.setBackgroundColor(Color.parseColor("#2672C3"))
        }
    }

    companion object {
        const val DEFAULT_CELL_SIZE = 145
        const val DEFAULT_CELL_MARGIN = 5
    }
}