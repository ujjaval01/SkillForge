package com.uv.skillforge.data.repository

import com.uv.skillforge.data.model.SkillforgeResponse
import com.uv.skillforge.data.remote.SkillforgeApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class SkillforgeRepository {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val apiService: SkillforgeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SkillforgeApiService.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SkillforgeApiService::class.java)
    }

    suspend fun getSkillforgeData(): SkillforgeResponse {
        return apiService.getSkillforgeData()
    }
}
