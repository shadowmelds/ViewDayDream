package dev.shadowmeld.viewdaydream.network.service

import dev.shadowmeld.viewdaydream.data.ConnectionURL
import dev.shadowmeld.viewdaydream.model.BaseResultInfo
import dev.shadowmeld.viewdaydream.model.MusicListDetailInfo
import dev.shadowmeld.viewdaydream.model.PersonalizedMusicListInfo
import dev.shadowmeld.viewdaydream.network.api.MusicListDetailApi
import dev.shadowmeld.viewdaydream.network.api.PersonalizedMusicListApi
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicListDetailService (
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) {

    private var api = Retrofit.Builder()
        .baseUrl(ConnectionURL.NETEASE_CLOUD_MUSIC_API_BASE_URL)
        .client(client.newBuilder().build())
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(MusicListDetailApi::class.java)

    suspend fun requestMusicListDetail(id: String): Response<MusicListDetailInfo> = api.getPersonalized(id)

}