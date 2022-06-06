package id.rezajuliandri.github.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.rezajuliandri.core.R
import java.text.SimpleDateFormat
import java.util.*

fun String.firstUpper() =
    this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }

fun ImageView.loadImage(url: String, width: Int = 500, height: Int = 500) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().override(width, height))
        .error(R.drawable.ic_error)
        .centerCrop()
        .into(this)
}

fun formatDate(date: String): String? {
    val dateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(date)
    return dateTime?.let {
        SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }.format(it)
    }
}