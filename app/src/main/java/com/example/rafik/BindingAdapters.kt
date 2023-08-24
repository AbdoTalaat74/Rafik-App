package com.example.rafik

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imgUri")
fun bindImageUri(imageView: ImageView, uri: Uri?) {
    uri?.let {
        imageView.setImageURI(it)
    }
}