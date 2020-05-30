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
import kotlinx.android.synthetic.main.fragment_contrast.*

class ContrastFragment : Fragment() {
    private var Photo: Bitmap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contrast, container, false)
    }

    private fun checkingResultSaved() {
        (activity as SecondActivity).applyOrCancel.visibility = View.VISIBLE


        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility = View.INVISIBLE
            seekContrast.progress = 100
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility = View.INVISIBLE
            seekContrast.progress = 100
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Photo = null

        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        seekContrast.progress = 100
        textViewContrast.text = "Contrast 100%"

        seekContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress
                textViewContrast.text = "Contrast: $temp%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val contrastBimap = contrastFilter(Photo!!, seekContrast.progress.toDouble() / 100)
                (activity as SecondActivity).image_view.setImageBitmap(contrastBimap)
                checkingResultSaved()
            }
        })

    }

    private fun contrastFilter(originBitmap: Bitmap, coefficient: Double): Bitmap {
        val width = originBitmap.width
        val height = originBitmap.height


        val pixelsArray = IntArray(width * height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val pallete = IntArray(256)

        var blue: Int
        var red: Int
        var green: Int

        var avg = 0
        var temp = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                red = Color.red(pixelsArray[y * width + x])
                green = Color.green(pixelsArray[y * width + x])
                blue = Color.blue(pixelsArray[y * width + x])

                avg += (red * 0.299 + green * 0.587 + blue * 0.114).toInt()


            }
        }
        avg /= (height * width)
        for (i in 0 until 256) {
            temp = (avg + coefficient * (i - avg)).toInt()
            if (temp < 0) {
                temp = 0
            } else {
                if (temp > 255) {
                    temp = 255
                }
            }
            pallete[i] = temp
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                red = Color.red(pixelsArray[y * width + x])
                green = Color.green(pixelsArray[y * width + x])
                blue = Color.blue(pixelsArray[y * width + x])

                pixelsArray[y * width + x] = Color.rgb(pallete[red], pallete[green], pallete[blue])

            }
        }
        return Bitmap.createBitmap(pixelsArray, width, height, Bitmap.Config.ARGB_8888)
    }

}
