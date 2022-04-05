package dev.shadowmeld.viewdaydream.network.api

import dev.shadowmeld.viewdaydream.model.BaseResultInfo
import dev.shadowmeld.viewdaydream.model.PersonalizedMusicListInfo
import retrofit2.Response
import retrofit2.http.GET

interface PersonalizedMusicListApi {

    @GET("personalized")
    suspend fun getPersonalized(): Response<BaseResultInfo<List<PersonalizedMusicListInfo>>>
}