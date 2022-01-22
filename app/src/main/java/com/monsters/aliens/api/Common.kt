package com.monsters.aliens.api

object Common {
    private const val BASE_URL = "https://rickandmortyapi.com/"
    val service: MyApi
        get() = RetrofitClient.getClient(BASE_URL).create(MyApi::class.java)
}