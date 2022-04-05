package dev.shadowmeld.viewdaydream.media

import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalMusicInfo

object PlayList {

    private var playListListener: PlayListListener? = null

    fun addUpdateListener(playListListener: PlayListListener) {
        this.playListListener = playListListener
    }

    private var currentPlayMusic: LocalMusicInfo? = null

    fun setCurrentPlayMusic(localMusicInfo: LocalMusicInfo) {
        this.currentPlayMusic = localMusicInfo
        playListListener?.onCurrentPlayUpdate(localMusicInfo)
    }
}

interface PlayListListener {
    fun onCurrentPlayUpdate(currentPlayMusic: LocalMusicInfo)
}