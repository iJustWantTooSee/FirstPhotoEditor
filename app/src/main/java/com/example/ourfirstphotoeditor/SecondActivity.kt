package com.example.ourfirstphotoeditor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.activity_second.*
import java.io.IOException
import java.util.*

public class SecondActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val uri: Uri = intent.getParcelableExtra("imageUri")
        if (uri!=null) {

            val compressedImage=decodeSampledBitmapFromResource(uri, 1024, 1024, this)
            image_view.setImageBitmap(compressedImage)
        }
        backMainActivity.setOnClickListener {
            var intent = Intent(SecondActivity@this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonSave.setOnClickListener {
            //сохранить изображение
            val originBitmap=(image_view.getDrawable() as BitmapDrawable).bitmap
            savingPhoto(originBitmap)
        }

        applyOrCancel.visibility = View.INVISIBLE

        rotate.setOnClickListener(){
            replaceFragment(RotateFragment())
        }
        filters.setOnClickListener(){
            replaceFragment(FiltersFragment())
        }
        bright.setOnClickListener(){
            replaceFragment(BrightFragment())
        }
        scale.setOnClickListener(){
            replaceFragment(ScaleFragment())
        }
        blackWhite.setOnClickListener(){
            replaceFragment(BlackWhiteFragment())
        }
        contrast.setOnClickListener(){
            replaceFragment(ContrastFragment())
        }
        soap.setOnClickListener(){
            replaceFragment(SoapFragment())
        }
        mask.setOnClickListener(){
            replaceFragment(MaskFragment())
        }

        backMainActivity.setOnClickListener{
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.exitTitle))
                .setMessage(getString(R.string.exitMessage))
                    //If you leave your result will not be saved.
                .setCancelable(true)
            builder.setPositiveButton(getString(R.string.yes)){
                dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton(getString(R.string.no)){
                dialog, which -> dialog.cancel()
            }
            builder.create().show()
        }
        buttonSave.setOnClickListener{
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            //Save image?
            builder.setTitle(getString(R.string.save))
                .setCancelable(true)
            builder.setPositiveButton(getString(R.string.yes)){
                    dialog, which ->
                savingPhoto((image_view.drawable as BitmapDrawable).bitmap)
            }
            builder.setNegativeButton(getString(R.string.no)){
                    dialog, which -> dialog.cancel()
            }
            builder.create().show()
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height
            val halfWidth: Int = width

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(
        currentUri: Uri,
        reqWidth: Int,
        reqHeight: Int,
        context: Context
    ): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val bitmap =BitmapFactory.Options().run {
            val stream = context.contentResolver.openInputStream(currentUri)
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(stream, null, this)

            inSampleSize=calculateInSampleSize(this, reqWidth, reqHeight)

            inJustDecodeBounds = false

            val newBitmap = context.contentResolver.openInputStream(currentUri)
            BitmapFactory.decodeStream(newBitmap, null, this)
        }

       return bitmap
    }


    private fun savingPhoto(originBitmap:Bitmap){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMG_$timeStamp"

        try {
            MediaStore.Images.Media.insertImage(contentResolver, originBitmap, imageFileName, "Image of $title")
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    public fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fr_place, fragment)
        transaction.commit()
    }
}
