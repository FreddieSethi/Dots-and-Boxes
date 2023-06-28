package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.util.AttributeSet
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import org.example.student.dotsboxgame.ComputerAIPlayer
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import java.lang.Math.*

class Interface : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)


    private val gameChangeListener = object : DotsAndBoxesGame.GameChangeListener {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    private var gameOverListener = object : DotsAndBoxesGame.GameOverListener {
        override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
            invalidate()
        }
    }

    // game board size variable
    var studentGame: StudentDotsBoxGame =
        StudentDotsBoxGame(6, 6, listOf(HumanPlayer(), ComputerAIPlayer()))
        set(value) {
            // restart button clicked listeners
            field.removeOnGameChangeListener(gameChangeListener)
            field.removeOnGameOverListener(gameOverListener)
            field = value

            value.addOnGameChangeListener(gameChangeListener)
            value.addOnGameOverListener(gameOverListener)
        }


    init {
        studentGame.addOnGameChangeListener(gameChangeListener)
    }

    // paint variables

    private var dotPaint: Paint = Paint().apply {
        // Controls the size of the dot
        strokeWidth = 30f
        //set the paint color
        color = Color.BLACK
        strokeCap = Paint.Cap.ROUND
    }

    private var linePaint: Paint = Paint().apply {
        style = Style.STROKE
        // Controls the size of the line
        strokeWidth = 5f
        color = Color.GRAY
    }

    private var clickedLinePaint: Paint = Paint().apply {
        style = Style.STROKE
        // Controls the size of the dot
        strokeWidth = 15f
        strokeCap = Paint.Cap.SQUARE
        color = Color.BLACK
    }

    private var player1Paint: Paint = Paint().apply {
        textSize = 80f
        color = Color.BLUE
        typeface = Typeface.SANS_SERIF
    }

    private var player2Paint: Paint = Paint().apply {
        textSize = 80f
        color = Color.RED
        typeface = Typeface.SANS_SERIF
    }

    private var backPaint: Paint = Paint().apply {
        // Set up the paint style
        style = Style.FILL
        color = Color.WHITE
    }


    // variables for amount of columns / rows and positioning
    private val colCount = studentGame.columns
    private val rowCount = studentGame.rows
    private var sep: Float = 0f
    private var offsetX: Float = 0f
    private var offsetY: Float = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        val xSep = w / (colCount + 1.5f)
        val ySep = h / (rowCount + 1.5f)
        sep = minOf(xSep, ySep)
        offsetX = (w - (sep * colCount)) / 2f
        offsetY = (h - (sep * rowCount)) / 1.4f
    }

    // listeners
    private val gestureDetector = GestureDetectorCompat(context, MyGestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)

    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }


        override fun onSingleTapUp(e: MotionEvent): Boolean {

            val xCoordinate = e.x - offsetX
            val yCoordinate = e.y - offsetY

            val closestHorizontalLine = round(yCoordinate / sep)
            val closestVerticalLine = round(xCoordinate / sep)

            val horizontalDistance = abs(xCoordinate - closestVerticalLine * sep)
            val verticalDistance = abs(yCoordinate - closestHorizontalLine * sep)

            val lineXTouched: Int
            val lineYTouched: Int

            if (horizontalDistance > verticalDistance) { // tapped a horizontal line
                lineXTouched = floor((xCoordinate / sep).toDouble()).toInt()
                lineYTouched = closestHorizontalLine * 2
            } else { // tapped a vertical line
                lineXTouched = closestVerticalLine
                lineYTouched = floor((yCoordinate / sep).toDouble()).toInt() * 2 + 1
            }

            if (studentGame.lines.isValid(lineXTouched, lineYTouched)) {
                val line = studentGame.lines[lineXTouched, lineYTouched]
                // check that line isn't drawn yet
                if (line.isDrawn) {
                    return true
                }
                line.drawLine()
            }
            return super.onSingleTapUp(e)
        }
    }

    override fun onDraw(canvas: Canvas) {

        // filled in boxes colour
        for (x in 0 until colCount) {
            for (y in 0 until rowCount) {

                val boxOwner: Paint =
                    if (studentGame.boxes[x, y].owningPlayer == studentGame.players[0]) {
                        player1Paint
                    } else if (studentGame.boxes[x, y].owningPlayer == studentGame.players[1]) {
                        player2Paint
                    } else {
                        backPaint
                    }
                canvas.drawRect(
                    x * sep + offsetX,
                    y * sep + offsetY,
                    (x + 1) * sep + offsetX,
                    (y + 1) * sep + offsetY,
                    boxOwner
                )
            }
        }

        // displaying horizontal lines
        for (x in 0 until colCount) {
            for (y in 0..rowCount) {

                val lineHorizontalClicked = if (studentGame.lines[x, y * 2].isDrawn) {
                    clickedLinePaint

                } else {
                    linePaint

                }

                canvas.drawLine(
                    x * sep + offsetX,
                    y * sep + offsetY,
                    (x + 1) * sep + offsetX,
                    y * sep + offsetY,
                    lineHorizontalClicked
                )
            }
        }
        // displaying vertical lines
        for (x in 0..colCount) {
            for (y in 0 until rowCount) {

                val lineVerticalClicked = if (studentGame.lines[x, (y * 2) + 1].isDrawn) {
                    clickedLinePaint

                } else {
                    linePaint
                }

                canvas.drawLine(
                    x * sep + offsetX,
                    y * sep + offsetY,
                    (x) * sep + offsetX,
                    (y + 1) * sep + offsetY,
                    lineVerticalClicked
                )
            }
        }

        for (x in 0..colCount) {
            for (y in 0..rowCount) {
                canvas.drawPoint(x * sep + offsetX, y * sep + offsetY, dotPaint)
            }
        }

        canvas.drawText(
            "Player 1: ${studentGame.getScores()[0]}",
            offsetX + 100f,
            offsetY - 400f,
            player1Paint
        )
        canvas.drawText(
            "Computer AI: ${studentGame.getScores()[1]}",
            offsetX + 100,
            offsetY - 200f,
            player2Paint
        )
    }
}