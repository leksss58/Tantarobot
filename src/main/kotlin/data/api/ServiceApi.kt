package data.api

import retrofit2.http.GET

interface ServiceApi {
    @GET("endpoint") // Замените "endpoint" на реальный
    suspend fun fetchData(): Unit // Укажите ваш тип ответа
}