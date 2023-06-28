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
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer

class Interface : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    // game board size variable
    private var studentGame: StudentDotsBoxGame =
        StudentDotsBoxGame(6, 6, listOf(HumanPlayer(), HumanPlayer()))

    // paint variables
    private var dotPaint: Paint = Paint().apply {
        // Controls the size of the dot
        strokeWidth = 20f
        //set the paint color
        color = Color.WHITE
    }
    private var linePaint: Paint = Paint().apply {
        // Controls the size of the line
        strokeWidth = 5f
        color = Color.GRAY
    }
    private var clickedLinePaint: Paint = Paint().apply {
        // Controls the size of the dot
        strokeWidth = 10f
        strokeCap = Paint.Cap.SQUARE
        color = Color.RED
    }
    private var player1Paint: Paint = Paint().apply {
        textSize = 100f
        color = Color.BLUE
        //textAlign = Paint.Align.CENTER
        typeface = Typeface.SANS_SERIF
    }
    private var player2Paint: Paint = Paint().apply {
        textSize = 100f
        color = Color.RED
        //textAlign = Paint.Align.CENTER
        typeface = Typeface.SANS_SERIF
    }
    private var backPaint: Paint = Paint().apply {
        // Set up the paint style
        style = Style.FILL
        color = Color.BLACK
    }

    //private var xSep: Float = 150f
    //private var ySep: Float = 150f

    // variables
    val canvasWidth get() = width.toFloat()
    val canvasHeight get() = height.toFloat()
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

//    val gameChangeListenerImp = object : DotsAndBoxesGame.GameOverListener {
//         fun onGameChange(game : DotsAndBoxesGame){
//            invalidate()
//        }
//
//    }
//
//    private var gameOverListenerImp = object : DotsAndBoxesGame.GameChangeListener {
//         fun onGameOver(game: DotsAndBoxesGame) {
//            invalidate()
//        }
//    }


    private val gestureDetector = GestureDetectorCompat(context, MyGestureListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)

    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return super.onDown(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val tapX = e.x
            val tapY = e.y
            val lineX: Int
            val lineY: Int
            TODO("Revert x and y to line coordinates")
            TODO("Draw line using drawLine fucntion")
            //gets pixel x and y and calls drawline
            // minus offset didive byb box width or height
            // // game.board[x, y], boundinglines()

            val clickX = (canvasWidth - offsetX)/colCount
            val clickY = (canvasHeight - offsetY)/rowCount



            // for each everytline it find center find closest line and closest list

            var lines = studentGame.StudentBox(clickX.toInt(), clickY.toInt()).boundingLines()

            return super.onSingleTapUp(e)

        }
    }


    override fun onDraw(canvas: Canvas) {
        // draw the View
        // Measure the size of the canvas, we could take into account padding here
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()

        // Draw rectangle with drawRect(topLeftX, topLeftY, bottomRightX, bottomRightY, Paint)
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backPaint)


        //horizontal lines
        for (x in 1 until colCount) {
            for (y in 1..rowCount) {
                val line = studentGame.lines[x, y]
                val linePaint = when {
                    line.isDrawn -> clickedLinePaint
                    else -> linePaint
                }


                // if statement which checks if line has been tapped and if it has then the line changes stroke width and paint colour
                canvas.drawLine(
                    x * sep + offsetX,
                    y * sep + offsetY,
                    (x + 1) * sep + offsetX,
                    y * sep + offsetY,
                    linePaint
                )
            }
        }
        //vertical lines
        for (x in 1..colCount) {
            for (y in 1 until rowCount) {


                canvas.drawLine(
                    x * sep + offsetX,
                    y * sep + offsetY,
                    (x) * sep + offsetX,
                    (y + 1) * sep + offsetY,
                    linePaint
                )
            }
        }

        for (x in 1..colCount) {
            for (y in 1..rowCount) {
                canvas.drawPoint(x * sep + offsetX, y * sep + offsetY, dotPaint)
            }
        }



        canvas.drawText("Player 1: ${studentGame.getScores()[0]}", offsetX, offsetY - 200f, player1Paint)
        canvas.drawText("Computer: ${studentGame.getScores()[1]}", offsetX, offsetY, player2Paint)

        canvas.drawRect(100f, 300f, 200f, 200f, player1Paint)
        canvas.drawRect(100f, 600f, 200f, 800f, player2Paint)


    }
}