package com.example.ourfirstphotoeditor

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlin.math.min

class FiltersFragment : Fragment() {

    var Photo: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        filter0.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo)
        }

        filter1.setOnClickListener {
            val negativePicture=negativeFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(negativePicture)
        }

        filter2.setOnClickListener {
            val blackAndWhitePicture=blackWhiteImageFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(blackAndWhitePicture)
        }

        filter3.setOnClickListener {
            val grayScalePicture=grayscaleFilters(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(grayScalePicture)
        }

        filter4.setOnClickListener {
            val sepiaPicture=sepiaFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(sepiaPicture)
        }

        filter5.setOnClickListener {
            val contrastPicture=contrastFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(contrastPicture)
        }

        filter6.setOnClickListener {
            val redPicture=redFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(redPicture)
        }

        filter7.setOnClickListener {
            val greenPicture=greenFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(greenPicture)
        }

        filter8.setOnClickListener {
            val bluePicture=blueFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(bluePicture)
        }

        filter9.setOnClickListener {
            val yellowPicture = yellowFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(yellowPicture)
        }

        filter10.setOnClickListener {
            val pinkPicture=pinkFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(pinkPicture)
        }

        filter11.setOnClickListener {
            val azurePicture=azureFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(azurePicture)
        }
    }





    // ----------------------------------Фильтры------------------------------------\\

   private fun negativeFilter(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {

                blue= Color.blue(pixelsArray[y*width+x])
                red= Color.red(pixelsArray[y*width+x])
                green= Color.green(pixelsArray[y*width+x])

                temp[y*width+x]= Color.rgb(255-red, 255-green,255-blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }


    private fun blackWhiteImageFilter(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        val separator= 255/0.9/2*3
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

    private fun grayscaleFilters(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int
        var gray:Int = 0

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green= Color.green(pixelsArray[y*width+x])
                blue= Color.blue(pixelsArray[y*width+x])

                gray=(red*0.2126+green*0.7152+blue*0.0722).toInt()

                temp[y*width+x]= Color.rgb(gray, gray,gray)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)


    }


    private fun sepiaFilter(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var red: Int
        var r:Int
        var green: Int
        var g:Int
        var blue: Int
        var b:Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                r= Color.red(pixelsArray[y*width+x])
                g= Color.green(pixelsArray[y*width+x])
                b= Color.blue(pixelsArray[y*width+x])

                red =min(255,(r * 0.393 + g * 0.769 + b * 0.189).toInt())
                green =min(255,(r * 0.349 + g * 0.686 + b * 0.168).toInt())
                blue =min(255,(r * 0.272 + g * 0.534 + b * 0.131).toInt())

                temp[y*width+x]= Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)

    }

    private fun contrastFilter(originBitmap: Bitmap):Bitmap {
        val width=originBitmap.width
        val height=originBitmap.height
        var coefficient:Int =2

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val pallete=IntArray(256)

        var blue: Int
        var red: Int
        var green: Int

        var avg=0
        var temp=0
        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green=Color.green(pixelsArray[y*width+x])
                blue=Color.blue(pixelsArray[y*width+x])

                avg+=(red*0.299+green*0.587+blue*0.114).toInt()


            }
        }
        avg/=(height*width)
        for (i in 0 until 256){
            temp=(avg+coefficient*(i-avg)).toInt()
            if (temp<0){
                temp=0
            }
            else{
                if (temp>255){
                    temp=255
                }
            }
            pallete[i]=temp
        }

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green=Color.green(pixelsArray[y*width+x])
                blue=Color.blue(pixelsArray[y*width+x])

                pixelsArray[y*width+x]=Color.rgb(pallete[red],pallete[green],pallete[blue])

            }
        }
        return Bitmap.createBitmap(pixelsArray, width, height, Bitmap.Config.ARGB_8888)
    }

    fun redFilter(originBitmap: Bitmap): Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green=0
                blue=0

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }

    fun greenFilter(originBitmap: Bitmap): Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= 0
                green=Color.green(pixelsArray[y*width+x])
                blue=0

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }

    fun blueFilter(originBitmap: Bitmap): Bitmap {
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= 0
                green=0
                blue=Color.blue(pixelsArray[y*width+x])

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }

    fun yellowFilter(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green=Color.green(pixelsArray[y*width+x])
                blue=0

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }

    fun pinkFilter(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= Color.red(pixelsArray[y*width+x])
                green=0
                blue=Color.blue(pixelsArray[y*width+x])

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }

    fun azureFilter(originBitmap: Bitmap):Bitmap{
        val width=originBitmap.width
        val height=originBitmap.height

        val pixelsArray = IntArray(width*height)
        originBitmap.getPixels(pixelsArray, 0, width, 0, 0, width, height)
        val temp=IntArray(width*height)

        var blue: Int
        var red: Int
        var green: Int

        for (y in 0 until height){
            for (x in 0 until width)  {
                red= 0
                green=Color.green(pixelsArray[y*width+x])
                blue=Color.blue(pixelsArray[y*width+x])

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }


}
