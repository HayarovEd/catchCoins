package com.edurda77.catchcoins

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    private lateinit var tvPoints: TextView
    private lateinit var tvHighest: TextView
    private lateinit var ivNewHighest: ImageView
    private lateinit var restart: ImageButton
    private lateinit var close: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        tvPoints = findViewById(R.id.pointsTv)
        tvHighest = findViewById(R.id.highestTv)
        ivNewHighest = findViewById(R.id.highest)
        restart = findViewById(R.id.restart)
        close = findViewById(R.id.exit)
        val points = intent.extras?.getInt("points")?:0
        tvPoints.text = points.toString()
        val sharedPreferences = getSharedPreferences("my_pref", 0)
        var highest = sharedPreferences.getInt("highest", 0)
        if (points > highest) {
            ivNewHighest.visibility = View.VISIBLE
            highest = points
            val editor = sharedPreferences.edit()
            editor.putInt("highest", highest)
            editor.commit()
        }
        tvHighest.text = highest.toString()
        restart.setOnClickListener {
            val intent =  Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        close.setOnClickListener {
            finish()
        }
    }
}