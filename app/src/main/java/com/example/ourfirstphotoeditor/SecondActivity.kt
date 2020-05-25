package com.example.ourfirstphotoeditor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_second.*

public class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val uri: Uri = intent.getParcelableExtra("imageUri")
        if (uri!=null) {
            image_view.setImageURI(uri);
        }
        backMainActivity.setOnClickListener {
            var intent = Intent(SecondActivity@this, MainActivity::class.java)
            startActivity(intent)
        }

        applyOrCancel.visibility = View.INVISIBLE

        

        rotate.setOnClickListener(){
            replaceFragment(RotateFragment())
        }
        filters.setOnClickListener(){
            replaceFragment(FiltersFragment())
        }
        bright.setOnClickListener(){
            replaceFragment(BrightFragment())
        }
        scale.setOnClickListener(){
            replaceFragment(ScaleFragment())
        }
        segm.setOnClickListener(){
            replaceFragment(SegmentationFragment())
        }
        inter.setOnClickListener(){
            replaceFragment(InterpolationFragment())
        }
        retouch.setOnClickListener(){
            replaceFragment(RetouchingFragment())
        }
        mask.setOnClickListener(){
            replaceFragment(MaskFragment())
        }
        bitri.setOnClickListener(){
            replaceFragment(BiTriFiltrationFragment())
        }
    }

    public fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fr_place, fragment)
        transaction.commit()
    }

}
