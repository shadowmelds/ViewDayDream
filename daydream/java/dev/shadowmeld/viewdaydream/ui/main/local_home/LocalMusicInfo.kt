package dev.shadowmeld.viewdaydream.ui.main.local_home

import android.graphics.Bitmap
import android.net.Uri

data class LocalMusicInfo(
    val uri: Uri,
    val musicName: String,
    val musicArtist: String,
    val musicDuration: Int,
    val size: Int
)
