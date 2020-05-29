package com.example.ourfirstphotoeditor

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_mask.*

class MaskFragment : Fragment() {

    companion object{
        var amountGlobal: Float= 0.1f
        var thresholdGlobal: Int =0
        var rangeGlobal: Int=1

    }

   private var Photo: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mask, container, false)
    }

    private fun checkingResultSaved(){
        (activity as SecondActivity).applyOrCancel.visibility=View.VISIBLE


        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE

            textViewAmount.text = "Amount: 0.1"
            textViewRadius.text = "Radius: 1"
            textViewThreshold.text = "Threshold: 0"

            seekAmount.progress=1
            seekThreshold.progress=0
            seekRadius.progress=1

            amountGlobal = 0.1f
            rangeGlobal = 1
            thresholdGlobal = 1
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo =((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE

            textViewAmount.text = "Amount: 0.1"
            textViewRadius.text = "Radius: 1"
            textViewThreshold.text = "Threshold: 0"

            seekAmount.progress=1
            seekThreshold.progress=1
            seekRadius.progress=1

            amountGlobal = 0.1f
            rangeGlobal = 1
            thresholdGlobal = 0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Photo = null
        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }


        textViewAmount.text = "Amount: 0.1"
        textViewRadius.text = "Radius: 1"
        textViewThreshold.text = "Threshold: 0"

        seekAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewAmount.text = "Amount: ${(seekAmount.progress.toFloat() / 10)}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                amountGlobal = (seekAmount.progress.toFloat())/10
            }
        })


        seekRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewRadius.text = "Radius: ${seekRadius.progress}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                rangeGlobal = seekRadius.progress
            }
        })


        seekThreshold.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewThreshold.text = "Threshold: ${seekThreshold.progress}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                thresholdGlobal = seekThreshold.progress
            }
        })

        confirmButton.setOnClickListener {
            checkingResultSaved()
            var unsharpBitmap= unsharpMask(Photo!!, amountGlobal, thresholdGlobal, rangeGlobal)
            (activity as SecondActivity).image_view.setImageBitmap(unsharpBitmap)
        }

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


    private fun unsharpMask(
        originBitmap: Bitmap,
        amount:Float,
        threshold:Int,
        range: Int
    ):Bitmap {
        val height = originBitmap.height
        val width = originBitmap.width

        val originPixelsArray = IntArray(width * height)
        originBitmap.getPixels(originPixelsArray, 0, width, 0, 0, width, height)
        val newPixels=IntArray(width*height)


        val blurImage = boxBlur(originBitmap, range)

        val blurPixelsArray = IntArray(width*height)
        blurImage!!.getPixels(blurPixelsArray, 0, width, 0, 0, width, height)

        var orgRed = 0
        var orgGreen = 0
        var orgBlue = 0
        var blurredRed = 0
        var blurredGreen = 0
        var blurredBlue = 0
        var usmPixel = 0
        val alpha = -0x1000000 //transperency is not considered and always zero

        for (j in 0 until height) {
            for (i in 0 until width) {
                val origPixel = originPixelsArray[j*width+i]
                val blurredPixel = blurPixelsArray[j*width+i]

                //seperate RGB values of original and blurred pixels into seperate R,G and B values
                orgRed = origPixel shr 16 and 0xff
                orgGreen = origPixel shr 8 and 0xff
                orgBlue = origPixel and 0xff

                blurredRed = blurredPixel shr 16 and 0xff
                blurredGreen = blurredPixel shr 8 and 0xff
                blurredBlue = blurredPixel and 0xff

                //If the absolute val. of difference between original and blurred
                //values are greater than the given threshold add weighed difference
                //back to the original pixel. If the result is outside (0-255),
                //change it back to the corresponding margin 0 or 255

                if (Math.abs(orgRed - blurredRed) >= threshold) {
                    orgRed = (amount * (orgRed - blurredRed) + orgRed).toInt()
                    orgRed = if (orgRed > 255) 255 else if (orgRed < 0) 0 else orgRed
                }
                if (Math.abs(orgGreen - blurredGreen) >= threshold) {
                    orgGreen = (amount * (orgGreen - blurredGreen) + orgGreen).toInt()
                    orgGreen = if (orgGreen > 255) 255 else if (orgGreen < 0) 0 else orgGreen
                }
                if (Math.abs(orgBlue - blurredBlue) >= threshold) {
                    orgBlue = (amount * (orgBlue - blurredBlue) + orgBlue).toInt()
                    orgBlue = if (orgBlue > 255) 255 else if (orgBlue < 0) 0 else orgBlue
                }
                usmPixel = alpha or (orgRed shl 16) or (orgGreen shl 8) or orgBlue
                //usmImage.setRGB(i, j, usmPixel)
                newPixels[j*width+i]=usmPixel
            }
        }
        return Bitmap.createBitmap(newPixels, width, height, Bitmap.Config.ARGB_8888)
    }





}
