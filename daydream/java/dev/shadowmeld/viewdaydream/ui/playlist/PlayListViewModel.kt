package dev.shadowmeld.viewdaydream.ui.playlist

import androidx.lifecycle.MutableLiveData
import dev.shadowmeld.viewdaydream.data.home.DefaultMusicListDetailRepository
import dev.shadowmeld.viewdaydream.model.MusicListDetailInfo
import dev.shadowmeld.viewdaydream.network.service.MusicListDetailService
import dev.shadowmeld.viewdaydream.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PlayListViewModel : BaseViewModel() {

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gsonConverterFactory = GsonConverterFactory.create()
    private val musicListDetailRepository = DefaultMusicListDetailRepository(
        MusicListDetailService(
            okHttpClient,
            gsonConverterFactory
        ),
        Dispatchers.IO
    )

    var liveData = MutableLiveData<MusicListDetailInfo>()

    fun getMusicListDetail(id: String) {

        launch(
            {
                musicListDetailRepository.getMusicListDetailInfo(id)
                liveData.value = musicListDetailRepository.observerResult.value
            },
            {
                errorLiveData.postValue(it)
            },
            {
                loadingLiveData.postValue(false)
            }
        )
    }
}