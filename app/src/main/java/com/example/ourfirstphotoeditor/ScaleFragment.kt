package com.example.ourfirstphotoeditor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.TextView
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
            //EditorActivity.States.states.add(ivPhoto!!)
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
                TODO("Not yet implemented")
            }

        })


    }




}
