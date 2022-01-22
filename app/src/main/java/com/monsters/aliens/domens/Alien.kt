package com.monsters.aliens.domens

import com.monsters.aliens.api.Common
import com.monsters.aliens.api.MyApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


data class Alien(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val image: String?,
    val origin: String?,
    val planet: String?
) {
    override fun toString(): String {
        return "Alien(id=$id, name=$name, status=$status, species=$species, gender=$gender, image=$image, origin=$origin, planet=$planet)"
    }
}