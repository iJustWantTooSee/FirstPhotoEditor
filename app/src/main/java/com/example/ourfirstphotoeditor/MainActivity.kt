package com.example.ourfirstphotoeditor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAKE_PICTURE = 1
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val REQUEST_TAKE_PHOTO = 1
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        infAboutTeamButton.text="О команде"

        infAboutTeamButton.setOnClickListener {

            Toast.makeText(this@MainActivity, "Наша команда: Виктор, Дарья, Кирилл", Toast.LENGTH_LONG).show()
        }

        cameraButton.setOnClickListener {

         /*   storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ),
                getAlbumName()
            )*/

            var intent = Intent(MainActivity@this, SecondActivity::class.java)
            startActivity(intent)
            startActivityForResult(cameraIntent, TAKE_PICTURE)
        }

        takeFromGalleryButton.setOnClickListener {
            var intent = Intent(MainActivity@this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

   /* override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(photoURI)
        }
    }


    fun onClick(view: View?) {
        dispatchTakePictureIntent()
    }

    private fun handleSmallCameraPhoto(intent: Intent) {
        val extras = intent.extras
        mImageBitmap = extras!!["data"] as Bitmap?
        mImageView.setImageBitmap(mImageBitmap)
    }
*/
}
