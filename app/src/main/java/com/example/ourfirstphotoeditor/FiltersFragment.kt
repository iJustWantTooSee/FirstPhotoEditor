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

    companion object{
        var Photo: Bitmap? = null
        var thumbnailImage: Bitmap? = null
        var firstImage: Boolean =true
        var firstImageBitmap: Bitmap? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }


    private fun checkingResultSaved(){
        (activity as SecondActivity).applyOrCancel.visibility=View.VISIBLE


        (activity as SecondActivity).buttonCancel.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
        }

        (activity as SecondActivity).buttonApply.setOnClickListener {
            Photo=((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            (activity as SecondActivity).image_view.setImageBitmap(Photo!!)
            thumbnailImage = Bitmap.createScaledBitmap(Photo!!, 150, 150, false)

            imagePreview(thumbnailImage!!)
            (activity as SecondActivity).applyOrCancel.visibility=View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        Photo = null
        firstImage = true

        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
            thumbnailImage = Bitmap.createScaledBitmap(Photo!!, 150, 150, false)

            if (firstImage){
                firstImageBitmap= Photo
                firstImage=false
            }

            imagePreview(thumbnailImage!!)
        }

        filter0.setOnClickListener {
            (activity as SecondActivity).image_view.setImageBitmap(firstImageBitmap)
            checkingResultSaved()
        }

        filter1.setOnClickListener {
            val negativePicture=negativeFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(negativePicture)
            checkingResultSaved()
        }


        filter2.setOnClickListener {
            val grayScalePicture=grayscaleFilters(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(grayScalePicture)
            checkingResultSaved()
        }

        filter3.setOnClickListener {
            val sepiaPicture=sepiaFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(sepiaPicture)
            checkingResultSaved()
        }


        filter4.setOnClickListener {
            val redPicture=redFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(redPicture)
            checkingResultSaved()
        }

        filter5.setOnClickListener {
            val greenPicture=greenFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(greenPicture)
            checkingResultSaved()
        }

        filter6.setOnClickListener {
            val bluePicture=blueFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(bluePicture)
            checkingResultSaved()
        }

        filter7.setOnClickListener {
            val yellowPicture = yellowFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(yellowPicture)
            checkingResultSaved()
        }

        filter8.setOnClickListener {
            val pinkPicture=pinkFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(pinkPicture)
            checkingResultSaved()
        }

        filter9.setOnClickListener {
            val azurePicture=azureFilter(Photo!!)
            (activity as SecondActivity).image_view.setImageBitmap(azurePicture)
            checkingResultSaved()
        }



    }

    private fun imagePreview(bitmapPreview: Bitmap){
        imageViewFilter0.setImageBitmap(firstImageBitmap)
        imageViewFilter1.setImageBitmap(negativeFilter(bitmapPreview))
        imageViewFilter2.setImageBitmap(grayscaleFilters(bitmapPreview))
        imageViewFilter3.setImageBitmap(sepiaFilter(bitmapPreview))
        imageViewFilter4.setImageBitmap(redFilter(bitmapPreview))
        imageViewFilter5.setImageBitmap(greenFilter(bitmapPreview))
        imageViewFilter6.setImageBitmap(blueFilter(bitmapPreview))
        imageViewFilter7.setImageBitmap(yellowFilter(bitmapPreview))
        imageViewFilter8.setImageBitmap(pinkFilter(bitmapPreview))
        imageViewFilter9.setImageBitmap(azureFilter(bitmapPreview))

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



    private fun redFilter(originBitmap: Bitmap): Bitmap{
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

    private fun greenFilter(originBitmap: Bitmap): Bitmap{
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

    private fun blueFilter(originBitmap: Bitmap): Bitmap {
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

    private fun yellowFilter(originBitmap: Bitmap):Bitmap{
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

    private fun pinkFilter(originBitmap: Bitmap):Bitmap{
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
                red= (Color.red(pixelsArray[y*width+x]) *0.9).toInt()
                green=0
                blue=(Color.blue(pixelsArray[y*width+x])*1.2).toInt()
                if (blue>255){
                    blue=255
                }

                temp[y*width+x]=Color.rgb(red,green,blue)
            }
        }

        return Bitmap.createBitmap(temp, width, height, Bitmap.Config.ARGB_8888)
    }

    private fun azureFilter(originBitmap: Bitmap):Bitmap{
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
