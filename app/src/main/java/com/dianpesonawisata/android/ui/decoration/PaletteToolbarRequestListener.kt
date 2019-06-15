package com.dianpesonawisata.android.ui.decoration

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.CollapsingToolbarLayout

class PaletteToolbarRequestListener(private val toolbarLayout: CollapsingToolbarLayout) : RequestListener<Bitmap> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Bitmap>?,
        isFirstResource: Boolean
    ): Boolean {
        return false
    }

    override fun onResourceReady(
        resource: Bitmap?,
        model: Any?,
        target: Target<Bitmap>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        resource?.let {
            Palette.from(resource).generate { palette ->
                val swatch = palette?.darkVibrantSwatch
                val mutedColor = swatch?.rgb
                val mutedTextColor = swatch?.bodyTextColor
                mutedColor?.let {
                    toolbarLayout.setContentScrimColor(mutedColor)
                    toolbarLayout.setStatusBarScrimColor(mutedColor)
                }

                mutedTextColor?.let {
                    toolbarLayout.setCollapsedTitleTextColor(mutedTextColor)
                    toolbarLayout.setExpandedTitleColor(mutedTextColor)
                }
            }
        }
        return false
    }

}
