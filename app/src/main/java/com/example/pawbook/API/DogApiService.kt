package com.example.pawbook.API


import com.example.pawbook.DTO.BreedImagesResponse
import com.example.pawbook.DTO.BreedsApiResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("api/breeds/list/all")
    fun getAllBreeds(): Call<BreedsApiResponse>
    @GET("api/breed/{breed}/images")
    fun getBreedImages(@Path("breed", encoded = true) breed: String): Call<BreedImagesResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://dog.ceo/"

    val instance: DogApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DogApiService::class.java)
    }
}