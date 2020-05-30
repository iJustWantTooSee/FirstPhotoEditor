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
import kotlinx.android.synthetic.main.fragment_black_white.*

class BlackWhiteFragment : Fragment() {
    private var Photo: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_black_white, container, false)
    }

    private fun checkingResultSaved(){
        (activity as SecondActivity).applyOrCancel.visibility=View.VISIBLE


        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekBlackWhite.progress = 0
            textViewBlackWhite.text = "Swipe the slider to start making changes"
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo =((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekBlackWhite.progress = 0
            textViewBlackWhite.text = "Swipe the slider to start making changes"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Photo=null
        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        seekBlackWhite.progress = 1
        textViewBlackWhite.text = "Swipe the slider to start making changes"

        seekBlackWhite.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress*2
                textViewBlackWhite.text = "$temp%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val blackWhiteBitmap=blackWhiteImageFilter(Photo!!, 1-(seekBlackWhite.progress.toDouble())/100)
                (activity as SecondActivity).image_view.setImageBitmap(blackWhiteBitmap)
                checkingResultSaved()
            }
        })

    }

    private fun blackWhiteImageFilter(originBitmap: Bitmap, k:Double):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        val separator= 255/k/2*3
        var blue: Int
        var red: Int
        var green: Int
        var sumRGB=0

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green= Color.green(pixelsArray[y*width+x])
                blue= Color.blue(pixelsArray[y*width+x])

                sumRGB=red+green+blue

                if (sumRGB>separator){
                    temp[y*width+x]= Color.rgb(255, 255,255)
                }
                else{
                    temp[y*width+x]= Color.rgb(0, 0,0)
                }
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)

    }

}
