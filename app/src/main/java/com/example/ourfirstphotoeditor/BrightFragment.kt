package com.example.ourfirstphotoeditor

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_bright.*
import kotlinx.android.synthetic.main.fragment_bright.textViewBright
import kotlin.math.max
import kotlin.math.min


class BrightFragment : Fragment() {
   private var Photo: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bright, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        seekBright.progress=100
        textViewBright.text = "100% brightness"

        seekBright.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress
                textViewBright.text = "$temp% brightness"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val newBrightnessImage=changesImageBrightness(Photo!!, seekBright.progress.toDouble()/100)
                (activity as SecondActivity).image_view.setImageBitmap(newBrightnessImage)
                checkingResultSaved()
            }

        })

    }

    private fun checkingResultSaved(){
        (activity as SecondActivity).applyOrCancel.visibility=View.VISIBLE


        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekBright.progress=100
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo =((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekBright.progress=100
        }
    }

    ///----------------------------------Яркость--------------------------------------\\
    fun changesImageBrightness(originBitmap: Bitmap, k:Double):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int
        var originPixel: Int
        for (y in 0 until height){
            for (x in 0 until width)  {
                originPixel=pixelsArray[y*width+x]

                blue= Color.blue(originPixel)
                red= Color.red(originPixel)
                green= Color.green(originPixel)

                blue=(blue*k).toInt()
                blue= min(255, max(0,blue))

                red=(red*k).toInt()
                red= min(255, max(0,red))

                green=(green*k).toInt()
                green= min(255, max(0,green))

                temp[y*width+x]= Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)

    }



}
