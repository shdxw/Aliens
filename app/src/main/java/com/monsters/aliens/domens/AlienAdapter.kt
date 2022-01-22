package com.monsters.aliens.domens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.monsters.aliens.R
import com.monsters.aliens.ui.person.CustomDialogFragment
import com.squareup.picasso.Picasso

class AlienAdapter(private var alienList: MutableList<Alien>, private var manager: FragmentManager, private var del: Boolean):RecyclerView.Adapter<AlienAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
        val imageAlien: ImageView = itemView.findViewById(R.id.imageAlien)

        fun bind(alien: Alien) {
            itemView.setOnClickListener {
                showDialog(manager, alien)
            }
        }
    }
    fun showDialog(childFragmentManager: FragmentManager, alien:Alien) {
        CustomDialogFragment(alien, this, del).show(
                childFragmentManager, "")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount() = alienList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView.text = alienList[position].name
        holder.smallTextView.text = alienList[position].status
        Picasso.get().load(alienList[position].image).into(holder.imageAlien)
        holder.bind(alienList[position])
    }

    fun delete(elem: Int): Boolean {
        return alienList.removeIf { it.id == elem}
    }

    fun add(list: MutableList<Alien>): Boolean {
        return alienList.addAll(list)
    }

}