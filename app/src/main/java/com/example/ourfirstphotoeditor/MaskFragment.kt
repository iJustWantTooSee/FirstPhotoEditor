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
        var Photo: Bitmap? = null
        var amount: Float= 0.1f
        var threshold: Int =0
        var range: Int=1

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mask, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }


        textViewAmount.text = "Amount: 0.1"
        textViewRadius.text = "Radius: 1"
        textViewThreshold.text = "Threshold: 0"

        seekAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                TODO("Not yet implemented")
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        seekRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                TODO("Not yet implemented")
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        seekThreshold.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                TODO("Not yet implemented")
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


    }

    private fun boxBlur(originBitmap: Bitmap): Bitmap? {
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
        val newColors = IntArray(w)
        for (y in 0 until h) {
            var hits = 0
            var r: Long = 0
            var g: Long = 0
            var b: Long = 0
            for (x in -halfRange until w) {
                val oldPixel = x - halfRange - 1
                if (oldPixel >= 0) {
                    val color = pixels[index + oldPixel]
                    if (color != 0) {
                        r -= Color.red(color).toLong()
                        g -= Color.green(color).toLong()
                        b -= Color.blue(color).toLong()
                    }
                    hits--
                }
                val newPixel = x + halfRange
                if (newPixel < w) {
                    val color = pixels[index + newPixel]
                    if (color != 0) {
                        r += Color.red(color).toLong()
                        g += Color.green(color).toLong()
                        b += Color.blue(color).toLong()
                    }
                    hits++
                }
                if (x >= 0) {
                    newColors[x] = Color.argb(0xFF, (r / hits).toInt(), (g / hits).toInt(), (b / hits).toInt());
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
        val oldPixelOffset = -(halfRange + 1) * w
        val newPixelOffset = halfRange * w
        for (x in 0 until w) {
            var hits = 0
            var r: Long = 0
            var g: Long = 0
            var b: Long = 0
            var index = -halfRange * w + x
            for (y in -halfRange until h) {
                val oldPixel = y - halfRange - 1
                if (oldPixel >= 0) {
                    val color = pixels[index + oldPixelOffset]
                    if (color != 0) {
                        r -= Color.red(color).toLong()
                        g -= Color.green(color).toLong()
                        b -= Color.blue(color).toLong()
                    }
                    hits--
                }
                val newPixel = y + halfRange
                if (newPixel < h) {
                    val color = pixels[index + newPixelOffset]
                    if (color != 0) {
                        r += Color.red(color).toLong()
                        g += Color.green(color).toLong()
                        b += Color.blue(color).toLong()
                    }
                    hits++
                }
                if (y >= 0) {
                    newColors[y] = Color.argb(0xFF, (r / hits).toInt(), (g / hits).toInt(), (b / hits).toInt());
                }
                index += w
            }
            for (y in 0 until h) {
                pixels[y * w + x] = newColors[y]
            }
        }
    }


    private fun unsharpMask(
        originBitmap: Bitmap
    ):Bitmap {
        val height = originBitmap.height
        val width = originBitmap.width

        val originPixelsArray = IntArray(width * height)
        originBitmap.getPixels(originPixelsArray, 0, width, 0, 0, width, height)
        val newPixels=IntArray(width*height)


        val blurImage = boxBlur(originBitmap)

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
