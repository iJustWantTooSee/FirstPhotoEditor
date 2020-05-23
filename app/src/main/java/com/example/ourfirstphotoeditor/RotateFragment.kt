package com.example.ourfirstphotoeditor

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_rotate.*


class RotateFragment : Fragment() {

    var Photo: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        seekRotate.progress=180
        textViewRotate.text = "0°"



        seekRotate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress-180
                textViewRotate.text = "$temp°"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val compressedImage= Bitmap.createScaledBitmap(Photo!!, 720, 1280, false)
                val newBitmap=rotatePicture(compressedImage,seekRotate.progress.toDouble()-180)
                (activity as SecondActivity).image_view.setImageBitmap(newBitmap)
            }

        })

    }


    fun rotatePicture(originBitmap: Bitmap, degrees: Double):Bitmap {
        val width: Int = originBitmap.getWidth()
        val height: Int = originBitmap.getHeight()

        val colorArray = IntArray(width * height)
        val newColorArray = IntArray(width * height)

        originBitmap.getPixels(colorArray, 0, width, 0, 0, width, height)

        val angle = Math.toRadians(degrees)
        val sin = Math.sin(angle)
        val cos = Math.cos(angle)

        val xCenter = 0.5 * (width - 1) // point to rotate about
        val yCenter = 0.5 * (height - 1) // center of image

        // rotation
        for (y in 0 until height){
            for (x in 0 until width)  {
                val a = x - xCenter
                val b = y - yCenter
                val xCurrent = (+a * cos - b * sin + xCenter).toInt()
                val yCurrent = (+a * sin + b * cos + yCenter).toInt()
                if (xCurrent >= 0 && xCurrent < width && yCurrent >= 0 && yCurrent < height) {
                    //newImage.setPixel(x, y, originBitmap.getPixel(xCurrent, yCurrent))
                    newColorArray[(y*width)+x]=colorArray[(yCurrent*width)+xCurrent]
                }
            }
        }
       return Bitmap.createBitmap(newColorArray, width, height, Bitmap.Config.ARGB_8888)
    }

}
