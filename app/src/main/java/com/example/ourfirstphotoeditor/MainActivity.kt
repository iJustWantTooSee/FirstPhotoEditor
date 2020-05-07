package com.example.ourfirstphotoeditor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        infAboutTeamButton.text="О команде"

        infAboutTeamButton.setOnClickListener {

            Toast.makeText(this@MainActivity, "Test", Toast.LENGTH_LONG).show()
        }

        cameraButton.setOnClickListener {
            var intent = Intent(MainActivity@this, SecondActivity::class.java)
            startActivity(intent)
        }

        takeFromGalleryButton.setOnClickListener {
            var intent = Intent(MainActivity@this, SecondActivity::class.java)
            startActivity(intent)
        }
    }


}
