package com.monsters.aliens.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.monsters.aliens.R
import com.monsters.aliens.api.Common
import com.monsters.aliens.domens.Alien
import com.monsters.aliens.domens.AlienAdapter
import com.monsters.aliens.ui.dashboard.DashboardFragment
import com.monsters.aliens.ui.dashboard.DashboardViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : DashboardFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mService = Common.service
        recyclerAlienList = root.findViewById(R.id.recyclerAlienList2)
        layoutManager = LinearLayoutManager(context)
        recyclerAlienList.layoutManager = layoutManager
        getAllAlienList()
        return root
    }

    override fun writeListView(response: String): MutableList<Alien> {
        var aliens: MutableList<Alien>  = mutableListOf()
        println(response)
        val gson = Gson()
        val array = gson.fromJson(response, JsonArray::class.java)
        for (jsonElement in array) {
            val jsAlien: JsonObject = jsonElement.asJsonObject
            aliens.add(Alien(jsAlien.get("id").asInt,
                    jsAlien.get("name").asString,
                    jsAlien.get("status").asString,
                    jsAlien.get("species").asString,
                    jsAlien.get("gender").asString,
                    jsAlien.get("image").asString,
                    jsAlien.get("origin").asJsonObject.get("name").asString,
                    jsAlien.get("location").asJsonObject.get("name").asString,
            ))
        }
        return aliens
    }

    override fun getAllAlienList() {
        val sharedPreference = context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var set = sharedPreference?.getStringSet("values", mutableSetOf())
        if (set != null) {
            if (set.isNotEmpty()) {
                println(set.toString())
                mService.getFavorPosts(set.toString()).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println("bad")
                        println(t.printStackTrace())
                        println(call.request().url())
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            println("norm")

                            var adapter = AlienAdapter(writeListView(response.body()?.string().toString()).toMutableList(),
                                    childFragmentManager,false)
                            recyclerAlienList.adapter = adapter

                        } else {
                            println(response.message())
                            println(response.body())
                        }

                    }
                })
            }

        }
    }
}