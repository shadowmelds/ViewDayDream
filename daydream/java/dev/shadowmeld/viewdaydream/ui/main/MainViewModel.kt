package dev.shadowmeld.viewdaydream.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shadowmeld.viewdaydream.data.SharedPreferencesValues
import dev.shadowmeld.viewdaydream.ui.BaseViewModel
import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalMusicInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    /**
     *  获取Daydream 的模式 本地模式或者网易云模式
     */

    private var sharedPref: SharedPreferences? = null

    fun loadData(sharedPref: SharedPreferences) {
        this.sharedPref = sharedPref
    }

    val daydreamMode: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    var isReady = false

    fun getDayDreamMode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                daydreamMode.postValue(sharedPref?.getBoolean(SharedPreferencesValues.DAYDREAM_MODE, false))
            }
        }
    }

    var currentMusic: ((musicData: LocalMusicInfo) -> Unit)? = null

    fun setUpdateCurrentMusic(
        currentMusic: (musicData: LocalMusicInfo) -> Unit
    ) {
        this.currentMusic = currentMusic
    }
}