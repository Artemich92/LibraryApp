package ru.artkolest.libraryapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.artkolest.booksapp.data.entities.ResponceEntity

interface Api {

    @GET("books?")
    suspend fun getAllBooks(
        @Query("page") page: Int
    ): ResponceEntity

        companion object{
        private const val BASE_URL = "https://demo.api-platform.com/"
        operator fun invoke():Api= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}