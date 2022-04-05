package dev.shadowmeld.viewdaydream.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.shadowmeld.viewdaydream.data.home.DefaultPersonalizedMusicListRepository
import dev.shadowmeld.viewdaydream.model.BaseResultInfo
import dev.shadowmeld.viewdaydream.model.PersonalizedMusicListInfo
import dev.shadowmeld.viewdaydream.network.service.PersonalizedMusicListService
import dev.shadowmeld.viewdaydream.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HomeViewModel : BaseViewModel() {

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gsonConverterFactory = GsonConverterFactory.create()
    private val personalizedMusicListRepository = DefaultPersonalizedMusicListRepository(
        PersonalizedMusicListService(
            okHttpClient,
            gsonConverterFactory
        ),
        Dispatchers.IO
    )

    var liveData = MutableLiveData<BaseResultInfo<List<PersonalizedMusicListInfo>>>()

    fun getPersonalizedMusicList() {

        launch(
            {
                personalizedMusicListRepository.getPersonalizedMusicListInfo()
                liveData.value = personalizedMusicListRepository.observerResult.value
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