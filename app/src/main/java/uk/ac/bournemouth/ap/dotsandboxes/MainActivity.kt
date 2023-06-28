package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame

class MainActivity : AppCompatActivity() {

    fun restart(view: View) {
        val Interface = findViewById<Interface>(R.id.interface1)
        Interface.studentGame = StudentDotsBoxGame(
            Interface.studentGame.columns,
            Interface.studentGame.rows,
            Interface.studentGame.players
        )
        Interface.invalidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button).callOnClick()
    }
}
