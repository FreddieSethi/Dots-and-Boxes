package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame

class ComputerAIPlayer : ComputerPlayer() {

    override fun makeMove(studentGame: DotsAndBoxesGame) {
        val unDrawn = studentGame.lines.filter { !it.isDrawn }
        val line = unDrawn.toList().random()
        line.drawLine()
    }
}