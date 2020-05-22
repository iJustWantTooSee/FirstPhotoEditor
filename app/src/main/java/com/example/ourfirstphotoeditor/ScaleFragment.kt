package com.example.ourfirstphotoeditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_scale.*

class ScaleFragment : Fragment() {

    var Photo: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        if (Photo == null) {
           Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }
        textViewScale.text = "100% of image"



        seekScale.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = 100 + progress
                textViewScale.text = "$temp% of image"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val trimmingTheEdges=trimmingTheEdgesPicture(Photo!!, 100-seekScale.progress)
                val newBitmap= resizeBilinear(trimmingTheEdges, 100f/(100f-seekScale.progress))
                (activity as SecondActivity).image_view.setImageBitmap(newBitmap)
            }

        })

    }


    fun trimmingTheEdgesPicture(originBitmap:Bitmap, percent: Int): Bitmap{
        var w1= originBitmap!!.width
        var w2=originBitmap!!.width*percent/100
        var h1=originBitmap!!.height
        var h2=originBitmap!!.height*percent/100

        val pixelsArray = IntArray(w1*h1)
        originBitmap.getPixels(pixelsArray, 0, w1, 0, 0, w1, h1)

        val temp = IntArray(w2 * h2)

        var xTemp: Int =(w1-w2)/2
        var yTemp: Int =(h1-h2)/2

        for (i in 0 until h2) {
            for (j in 0 until w2) {

                temp[i*w2+j]=pixelsArray[w1*(yTemp+i)+(xTemp+j)]
            }
        }

        return Bitmap.createBitmap(temp, w2, h2, Bitmap.Config.ARGB_8888)
    }


    fun resizeBilinear(originBitmap: Bitmap, k:Float): Bitmap {
        val w1 = originBitmap.width
        val h1=originBitmap.height

        val pixelsArray = IntArray(w1*h1)
        originBitmap.getPixels(pixelsArray, 0, w1, 0, 0, w1, h1)

        val w2=(originBitmap.width*k).toInt()
        val h2=(originBitmap.height*k).toInt()
        val temp = IntArray(w2 * h2)

        //We consider the square of pixels 2X2
        var a: Int          //upper left corner of the original image square
        var b: Int          //upper right corner of the original image square
        var c: Int          //lower left
        var d: Int          //дщцук кшпре (нижний правый)
        var x: Int
        var y: Int
        var index: Int


        val xRatio = (w1 - 1).toFloat() / w2
        val yRatio = (h1 - 1).toFloat() / h2
        var xDifferent: Float
        var yDifferent: Float
        var blue: Float
        var red: Float
        var green: Float
        var offset = 0

        for (i in 0 until h2) {
            for (j in 0 until w2) {
                x = (xRatio * j).toInt()
                y = (yRatio * i).toInt()
                xDifferent = xRatio * j - x
                yDifferent = yRatio * i - y
                index = y * w1 + x
                a = pixelsArray[index]
                b = pixelsArray[index + 1]
                c = pixelsArray[index + w1]
                d = pixelsArray[index + w1 + 1]

                // blue element
                // Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
                blue =
                    (a and 0xff) * (1 - xDifferent) * (1 - yDifferent) + (b and 0xff) * xDifferent *
                            (1 - yDifferent) + (c and 0xff) * yDifferent * (1 - xDifferent) + (d and 0xff) *
                            (xDifferent * yDifferent)

                // green element
                // Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
                green =
                    (a shr 8 and 0xff) * (1 - xDifferent) * (1 - yDifferent) + (b shr 8 and 0xff) * xDifferent *
                            (1 - yDifferent) + (c shr 8 and 0xff) * yDifferent * (1 - xDifferent) + (d shr 8 and 0xff) *
                            (xDifferent * yDifferent)

                // red element
                // Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
                red =
                    (a shr 16 and 0xff) * (1 - xDifferent) * (1 - yDifferent) + (b shr 16 and 0xff) * xDifferent *
                            (1 - yDifferent) + (c shr 16 and 0xff) * yDifferent * (1 - xDifferent) + (d shr 16 and 0xff) *
                            (xDifferent * yDifferent)


                temp[offset++] = -0x1000000 or  // hardcode alpha
                        (red.toInt() shl 16 and 0xff0000) or
                        (green.toInt() shl 8 and 0xff00) or
                        blue.toInt()
            }
        }


        return Bitmap.createBitmap(temp, w2, h2, Bitmap.Config.ARGB_8888)
    }



}
