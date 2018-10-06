package com.racjonalnytraktor.findme3.ui.main.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.fragment_profile.*
import android.widget.Toast
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.R.attr.data
import android.app.Activity.RESULT_OK
import android.net.Uri
import java.io.FileNotFoundException


class ProfileFragment: BaseFragment<MapMvp.View>() {

    val requestGallery = 1905

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFragment","onViewCreated")
        iconBack.setImageDrawable(IconicsDrawable(parentMvp.getCtx())
                .sizeDp(18)
                .color(Color.BLACK)
                .icon(GoogleMaterial.Icon.gmd_arrow_back))

        iconBack.setOnClickListener {
            parentMvp.getPresenter().onBackInFragmentClick("profile")
        }
        fabProfile.setImageDrawable(IconicsDrawable(parentMvp.getCtx())
                .icon(FontAwesome.Icon.faw_plus)
                .sizeDp(12)
                .color(Color.WHITE))
        fabProfile.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,requestGallery)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == requestGallery) {
            try {
                val imageUri = data?.data ?: Uri.EMPTY
                val imageStream = parentContext.contentResolver.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                imageProfile.setImageBitmap(selectedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        } else {
            //Toast.makeText(this@PostImage, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}