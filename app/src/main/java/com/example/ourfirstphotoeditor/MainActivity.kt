package com.example.ourfirstphotoeditor

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAKE_PICTURE = 1
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infAboutTeamButton.text="О команде"

        infAboutTeamButton.setOnClickListener {

            Toast.makeText(this@MainActivity, "Наша команда: Виктор, Дарья, Кирилл", Toast.LENGTH_LONG).show()
        }

        cameraButton.setOnClickListener {

            startActivityForResult(cameraIntent, TAKE_PICTURE)


            var intent = Intent(MainActivity@this, SecondActivity::class.java)
            startActivity(intent)
        }

        takeFromGalleryButton.setOnClickListener {
            var intent = Intent(MainActivity@this, SecondActivity::class.java)
            startActivity(intent)
        }
    }


}
