package com.uv.skillforge.data.remote

import com.uv.skillforge.data.model.SkillforgeResponse
import com.uv.skillforge.BuildConfig
import retrofit2.http.GET

interface SkillforgeApiService {
    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getSkillforgeData(): SkillforgeResponse

    companion object {
        const val BASE_URL = BuildConfig.API_URL
    }
}
