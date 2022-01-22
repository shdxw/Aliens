package com.monsters.aliens.ui.person

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.DialogFragment
import com.monsters.aliens.R
import com.monsters.aliens.domens.Alien
import com.squareup.picasso.Picasso
import com.monsters.aliens.domens.AlienAdapter


class CustomDialogFragment(val alien: Alien,val adapter: AlienAdapter, var butuse: Boolean) : DialogFragment() {

    lateinit var button: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView: View = inflater.inflate(R.layout.dialog, container, false)
        var aliveText = rootView.findViewById<TextView>(R.id.aliveText)
        var genderText = rootView.findViewById<TextView>(R.id.genderText)
        var startPlanetText = rootView.findViewById<TextView>(R.id.startPlanetText)
        var planetText = rootView.findViewById<TextView>(R.id.planetText)
        button = rootView.findViewById<Button>(R.id.changeButton)

        Picasso.get().load(alien.image).into(rootView.findViewById<ImageView>(R.id.imageView2))
        aliveText.text = alien.status
        genderText.text = alien.gender
        startPlanetText.text = alien.origin
        planetText.text = alien.planet

        if (butuse) {
            buttonUse()
        } else {
            buttonDel()
        }

        return rootView
    }

    fun buttonUse() {
        button.setOnClickListener {
            val sharedPreference = context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference?.edit()
            var set = sharedPreference?.getStringSet("values", mutableSetOf())
            set?.add(alien.id.toString())
            editor?.putStringSet("values", set)
            editor?.apply()
            dismiss()
        }
    }

    fun buttonDel() {
        button.text = "DEL"
        button.setOnClickListener {
            val sharedPreference = context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference?.edit()
            var set = sharedPreference?.getStringSet("values", mutableSetOf())
            var removed = alien.id?.let { it1 -> adapter.delete(it1) }
            set?.removeIf {it.equals(alien.id.toString())}
            editor?.putStringSet("values", set)
            editor?.apply()
            if (removed == true) {
                adapter.notifyDataSetChanged()
            }
            dismiss()
        }
    }
}