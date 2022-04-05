package dev.shadowmeld.viewdaydream.data.home

import androidx.lifecycle.MutableLiveData
import dev.shadowmeld.viewdaydream.model.BaseResultInfo
import dev.shadowmeld.viewdaydream.model.PersonalizedMusicListInfo
import dev.shadowmeld.viewdaydream.network.service.PersonalizedMusicListService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface PersonalizedMusicListRepository {

    suspend fun getPersonalizedMusicListInfo()

    val observerResult: MutableLiveData<BaseResultInfo<List<PersonalizedMusicListInfo>>>
}

class DefaultPersonalizedMusicListRepository (
    private val service: PersonalizedMusicListService,
    private val dispatcher: CoroutineDispatcher
) : PersonalizedMusicListRepository {

    private val result: MutableLiveData<BaseResultInfo<List<PersonalizedMusicListInfo>>> by lazy {
        MutableLiveData<BaseResultInfo<List<PersonalizedMusicListInfo>>>()
    }

    override suspend fun getPersonalizedMusicListInfo() {

        withContext(dispatcher) {
            val request = service.requestPersonalizedMusicListInfo()
            if (request.isSuccessful) {
                request.body()?.let {
                    result.postValue(it)
                }
            }
        }
    }

    override val observerResult: MutableLiveData<BaseResultInfo<List<PersonalizedMusicListInfo>>>
        get() = result

}