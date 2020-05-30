package com.example.ourfirstphotoeditor

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_soap.*

class SoapFragment : Fragment() {
    private var Photo: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_soap, container, false)
    }

    private fun checkingResultSaved(){
        (activity as SecondActivity).applyOrCancel.visibility=View.VISIBLE

        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekSoap.progress = 0
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo =((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
            seekSoap.progress = 0
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Photo=null

        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }


        seekSoap.progress = 0
        textViewSoap.text = "Blur: 0%"


        seekSoap.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress*2
                textViewSoap.text = "Blur: $temp%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val blurBitmap = boxBlur(Photo!!, seekSoap.progress)
                (activity as SecondActivity).image_view.setImageBitmap(blurBitmap)
                checkingResultSaved()
            }
        })

    }

    private fun boxBlur(originBitmap: Bitmap,
                        range:Int
    ): Bitmap? {
        assert(range and 1 == 0) { "Range must be odd." }


        val blurred = Bitmap.createBitmap(
            originBitmap.width, originBitmap.height,
            Bitmap.Config.ARGB_8888
        )


        val canvas = Canvas(blurred)
        val w = originBitmap.width
        val h = originBitmap.height


        val pixels = IntArray(originBitmap.width * originBitmap.height)
        originBitmap.getPixels(pixels, 0, w, 0, 0, w, h)

        boxBlurHorizontal(pixels, w, h, range / 2)

        boxBlurVertical(pixels, w, h, range / 2)

        canvas.drawBitmap(pixels, 0, w, 0.0f, 0.0f, w, h, true, null)
        return blurred
    }


    private fun boxBlurHorizontal(
        pixels: IntArray, w: Int, h: Int,
        halfRange: Int
    ) {
        var index = 0
        var hits = 0
        val alpha = -0x1000000
        var r: Long = 0
        var g: Long = 0
        var b: Long = 0
        var oldPixel=0
        var newPixel:Int
        var color:Int
        var colorNew:Int

        val newColors = IntArray(w)

        for (y in 0 until h) {
            hits = 0
            r= 0
            g = 0
            b = 0
            for (x in -halfRange until w) {
                oldPixel = x - halfRange - 1
                if (oldPixel >= 0) {
                    color = pixels[index + oldPixel]
                    if (color != 0) {
                        r -= color shr 16 and 0xff
                        g -= color shr 8 and 0xff
                        b -= color and 0xff
                    }
                    hits--
                }
                newPixel = x + halfRange
                if (newPixel < w) {
                    colorNew = pixels[index + newPixel]
                    if (colorNew != 0) {
                        r += colorNew shr 16 and 0xff
                        g += colorNew shr 8 and 0xff
                        b += colorNew and 0xff
                    }
                    hits++
                }
                if (x >= 0) {
                    newColors[x] = alpha or ((r / hits).toInt() shl 16) or ((g / hits).toInt() shl 8) or (b / hits).toInt()
                }
            }
            for (x in 0 until w) {
                pixels[index + x] = newColors[x]
            }
            index += w
        }
    }

    private fun boxBlurVertical(
        pixels: IntArray, w: Int, h: Int,
        halfRange: Int
    ) {
        val newColors = IntArray(h)
        val alpha = -0x1000000
        var hits = 0
        var r: Long = 0
        var g: Long = 0
        var b: Long = 0
        var index = 0
        var oldPixel=0
        var newPixel:Int
        var color:Int
        var colorNew:Int

        val oldPixelOffset = -(halfRange + 1) * w
        val newPixelOffset = halfRange * w
        for (x in 0 until w) {
            hits = 0
            r = 0
            g= 0
            b= 0
            index = -halfRange * w + x
            for (y in -halfRange until h) {
                oldPixel = y - halfRange - 1
                if (oldPixel >= 0) {
                    color = pixels[index + oldPixelOffset]
                    if (color != 0) {
                        r -= color shr 16 and 0xff
                        g -= color shr 8 and 0xff
                        b -= color and 0xff
                    }
                    hits--
                }
                newPixel = y + halfRange
                if (newPixel < h) {
                    colorNew = pixels[index + newPixelOffset]
                    if (colorNew != 0) {
                        r += colorNew shr 16 and 0xff
                        g += colorNew shr 8 and 0xff
                        b += colorNew and 0xff
                    }
                    hits++
                }
                if (y >= 0) {
                    newColors[y] = alpha or ((r / hits).toInt() shl 16) or ((g / hits).toInt() shl 8) or (b / hits).toInt()
                }
                index += w
            }
            for (y in 0 until h) {
                pixels[y * w + x] = newColors[y]
            }
        }
    }



}
