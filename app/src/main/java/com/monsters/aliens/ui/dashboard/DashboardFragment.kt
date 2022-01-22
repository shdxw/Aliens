package com.monsters.aliens.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.monsters.aliens.R
import com.monsters.aliens.api.Common
import com.monsters.aliens.api.MyApi
import com.monsters.aliens.domens.Alien
import com.monsters.aliens.domens.AlienAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class DashboardFragment : Fragment() {

    lateinit var dashboardViewModel: DashboardViewModel
    lateinit var mService: MyApi
    lateinit var recyclerAlienList: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var buttonPage: Button
    lateinit var listOfAliens: MutableList<Alien>
    var pageNum = 1;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        mService = Common.service
        recyclerAlienList = root.findViewById(R.id.recyclerAlienList2)
        buttonPage = root.findViewById(R.id.buttonPage)
        layoutManager = LinearLayoutManager(context)
        recyclerAlienList.layoutManager = layoutManager

        buttonPage.setOnClickListener(View.OnClickListener { view ->
            pageNum++
            addAlienList(pageNum)
            var pageString = "Page $pageNum"
            buttonPage.text = pageString
        })

        getAllAlienList()

        return root
    }

    open fun writeListView(response: String): MutableList<Alien> {
        var aliens: MutableList<Alien>  = mutableListOf()
        println(response)
        val gson = Gson()
        val jsonObject: JsonObject = gson.fromJson(response, JsonObject::class.java)
        val array = jsonObject.get("results").asJsonArray
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

    fun addAlienList(num: Int) {
        mService.getExtraPosts(num).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("bad")
                println(t.printStackTrace())
                println(call.request().url())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    println("norm")
                    var list = writeListView(response.body()?.string().toString()).toMutableList()
                    listOfAliens.addAll(list)
                    recyclerAlienList.adapter?.notifyDataSetChanged()

                } else {
                    println(response.message())
                    println(response.body())
                }

            }
        })
    }

    open fun getAllAlienList() {
        mService.getPosts().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("bad")
                println(t.printStackTrace())
                println(call.request().url())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    println("norm")
                    listOfAliens = writeListView(response.body()?.string().toString()).toMutableList()
                    var adapter = AlienAdapter(listOfAliens,
                            childFragmentManager,true)
                    adapter.notifyDataSetChanged()
                    recyclerAlienList.adapter = adapter

                } else {
                    println(response.message())
                    println(response.body())
                }

            }
        })
    }
}