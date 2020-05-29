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

    companion object{
        var Photo: Bitmap? = null
        var currentDegreesOfRotation: Int = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate, container, false)
    }

    private fun checkingResultSaved(){
        (activity as SecondActivity).applyOrCancel.visibility=View.VISIBLE


        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekRotate.progress = 45
            currentDegreesOfRotation = 0
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo =((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekRotate.progress = 45
            currentDegreesOfRotation = 0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        seekRotate.progress=45
        textViewRotate.text = "0°"



        seekRotate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress-45
                textViewRotate.text = "$temp°"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                currentDegreesOfRotation-=seekRotate.progress-45
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                currentDegreesOfRotation+=seekRotate.progress-45
                currentDegreesOfRotation %=360
                val newBitmap=rotatePicture(Photo!!, currentDegreesOfRotation.toDouble())
                (activity as SecondActivity).image_view.setImageBitmap(newBitmap)
                checkingResultSaved()
            }

        })

        buttonRotateLeft90.setOnClickListener() {
            currentDegreesOfRotation+=90
            currentDegreesOfRotation %=360
            val newBitmap=rotatePicture(Photo!!, currentDegreesOfRotation.toDouble())
            (activity as SecondActivity).image_view.setImageBitmap(newBitmap)
            checkingResultSaved()
        }
        buttonZero.setOnClickListener(){
            currentDegreesOfRotation = 0
            seekRotate.progress = 45
            val newBitmap=rotatePicture(Photo!!, currentDegreesOfRotation.toDouble())
            (activity as SecondActivity).image_view.setImageBitmap(newBitmap)
            checkingResultSaved()
        }
        buttonRotateRight90.setOnClickListener() {
            currentDegreesOfRotation-=90
            currentDegreesOfRotation %=360
            val newBitmap=rotatePicture(Photo!!, currentDegreesOfRotation.toDouble())
            (activity as SecondActivity).image_view.setImageBitmap(newBitmap)
            checkingResultSaved()
        }

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
                    newColorArray[(y*width)+x]=colorArray[(yCurrent*width)+xCurrent]
                }
            }
        }
       return Bitmap.createBitmap(newColorArray, width, height, Bitmap.Config.ARGB_8888)
    }

}
