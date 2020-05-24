package com.example.ourfirstphotoeditor

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_second.*

public class SecondActivity : AppCompatActivity() {


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
        segm.setOnClickListener(){
            replaceFragment(SegmentationFragment())
        }
        inter.setOnClickListener(){
            replaceFragment(InterpolationFragment())
        }
        retouch.setOnClickListener(){
            replaceFragment(RetouchingFragment())
        }
        mask.setOnClickListener(){
            replaceFragment(MaskFragment())
        }
        bitri.setOnClickListener(){
            replaceFragment(BiTriFiltrationFragment())
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

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

    public fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fr_place, fragment)
        transaction.commit()
    }

}
