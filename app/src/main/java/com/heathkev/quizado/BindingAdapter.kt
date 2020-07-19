package com.heathkev.quizado

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.heathkev.quizado.data.QuizListModel
import com.heathkev.quizado.ui.list.QuizListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<QuizListModel>?) {
    val adapter = recyclerView.adapter as QuizListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    val image = if (imgUrl != null) imgUrl.toUri().buildUpon().scheme("https")
        .build() else R.drawable.ic_unknown
    Glide.with(imgView.context)
        .load(image)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.placeholder_image)
        )
        .into(imgView)
}

@BindingAdapter("imageUrl")
fun bindImageUri(imgView: ImageView, imgUrl: Uri?) {
    val image = if (imgUrl != null && imgUrl != Uri.EMPTY) {
        imgUrl.buildUpon().scheme("https")
            ?.build()
    } else{
        R.drawable.ic_unknown
    }

    Glide.with(imgView.context)
        .load(image)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.placeholder_image)
        )
        .into(imgView)
}

@BindingAdapter("truncate")
fun bindTextView(textView: TextView, description: String) {
    var listDescription = description
    if (listDescription.length > 150) {
        listDescription = listDescription.substring(0, 150)
    }
    textView.text = "$listDescription..."
}

@BindingAdapter("convertLong")
fun bindTextView(textView: TextView, long: Long) {
    textView.text = long.toString()
}

@BindingAdapter("playerNames")
fun bindTextViewName(textView: TextView, name: String?) {
    textView.text = if (name.isNullOrEmpty()) {
        "anonymous"
    } else {
        name
    }
}