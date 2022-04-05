package dev.shadowmeld.viewdaydream.data.home

import androidx.lifecycle.MutableLiveData
import dev.shadowmeld.viewdaydream.model.MusicListDetailInfo
import dev.shadowmeld.viewdaydream.network.service.MusicListDetailService
import dev.shadowmeld.viewdaydream.util.logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface MusicListDetailRepository {

    suspend fun getMusicListDetailInfo(id: String)

    val observerResult: MutableLiveData<MusicListDetailInfo>
}

class DefaultMusicListDetailRepository(
    private val service: MusicListDetailService,
    private val dispatcher: CoroutineDispatcher
): MusicListDetailRepository {

    private val result: MutableLiveData<MusicListDetailInfo> by lazy {
        MutableLiveData<MusicListDetailInfo>()
    }

    override suspend fun getMusicListDetailInfo(id: String) {
        withContext(dispatcher) {
            logger("开始请求 playlist/detail?id=${id}")
            val request = service.requestMusicListDetail(id)
            if (request.isSuccessful) {
                logger("请求成功")
                request.body()?.let {
                    result.postValue(it)
                }
            }
        }
    }

    override val observerResult: MutableLiveData<MusicListDetailInfo>
        get() = result

}

