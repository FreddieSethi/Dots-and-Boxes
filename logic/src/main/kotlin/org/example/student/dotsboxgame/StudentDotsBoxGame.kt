package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

class StudentDotsBoxGame(val columns: Int, val rows: Int, players: List<Player>) :
    AbstractDotsAndBoxesGame() {

    override val players: List<Player> =
        players.toList() //__TODO("You will need to get players from your constructor")

    override var currentPlayer: Player = players[0]
    //__TODO("Determine the current player, like keeping" + "the index into the players list")

    override val boxes: Matrix<StudentBox> = MutableMatrix(columns, rows, ::StudentBox)

    //__TODO("Create a matrix initialized with your own box type")

    override val lines: SparseMatrix<StudentLine> = MutableSparseMatrix(
        columns + 1,
        rows * 2 + 1,
        { x, y -> x < columns || y % 2 == 1 },
        ::StudentLine
    )
    //__TODO("Create a matrix initialized with your own line type")

    override var isFinished: Boolean = false
    //__TODO("Provide this getter. Note you can make it a var to do so (with private set)")

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && !isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false
        //get() = __TODO("Provide this getter. Note you can make it a var to do so")

        private val isVertical = lineY % 2 == 1

        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                val box1: StudentBox?
                val box2: StudentBox?

                if (isVertical) {
                    val box1X = lineX - 1
                    val box2X = lineX
                    val boxY = lineY / 2
                    box1 = if (boxes.isValid(box1X, boxY)) boxes[box1X, boxY] else null
                    box2 = if (boxes.isValid(box2X, boxY)) boxes[box2X, boxY] else null
                } else {
                    val boxX = lineX
                    val box1Y = lineY / 2 - 1
                    val box2Y = lineY / 2

                    box1 = if (boxes.isValid(boxX, box1Y)) boxes[boxX, box1Y] else null
                    box2 = if (boxes.isValid(boxX, box2Y)) boxes[boxX, box2Y] else null
                }

                return Pair(box1, box2)
                //__TODO("You need to look up the correct boxes for this to work")
            }

        override fun drawLine() {
            //__TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player

            if (isDrawn) {
                throw IllegalStateException("The line is already drawn")
            } else {
                isDrawn = true
                var needsAnotherTurn = false
                for (box in adjacentBoxes.toList().filterNotNull()) {
                    if (box.boundingLines.all { it.isDrawn }) {
                        box.owningPlayer = currentPlayer
                        needsAnotherTurn = true
                    }
                }

                fireGameChange()

                if (needsAnotherTurn && boxes.all { it.owningPlayer != null }) {
                    isFinished = true
                    fireGameOver(
                        listOf(
                            Pair(players[0], getScores()[0]),
                            Pair(players[1], getScores()[1])
                        )
                    )

                } else if (!needsAnotherTurn) {
                    currentPlayer = if (currentPlayer == players[0]) {
                        players[1]
                    } else {
                        players[0]
                    }
                    if (currentPlayer != HumanPlayer()) {
                        playComputerTurns()
                    }
                }
            }
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null
        //__TODO("Provide this getter. Note you can make it a var to do so")

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() { //__TODO("Look up the correct lines from the game outer class")
                val topLine: StudentLine?
                val bottomLine: StudentLine?
                val leftLine: StudentLine?
                val rightLine: StudentLine?

                topLine = lines[boxX, boxY * 2]
                bottomLine = lines[boxX, ((boxY * 2) + 2)]
                leftLine = lines[boxX, ((boxY * 2) + 1)]
                rightLine = lines[boxX + 1, ((boxY * 2) + 1)]

                return listOf(topLine, leftLine, bottomLine, rightLine)
            }
    }
}