package com.example.ourfirstphotoeditor

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.fragment_bright.*
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Photo == null) {
            Photo = ((activity as SecondActivity)!!.image_view.drawable as BitmapDrawable).bitmap
        }

        seekContrast.progress = 1
        textViewContrast.text = "1.0 contrast"



        seekBright.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val temp = progress
                textViewContrast.text = "$temp% contrast"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

    }
}
