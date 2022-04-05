package dev.shadowmeld.viewdaydream.network.api

import dev.shadowmeld.viewdaydream.model.MusicListDetailInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicListDetailApi {

    @GET("playlist/detail")
    suspend fun getPersonalized(@Query("id") id: String): Response<MusicListDetailInfo>
}