package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.ItemCustomListBinding
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.ui.fragments.add.AddFragment

class CustomListItemAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    private lateinit var listProgress: List<String>

    class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding =
            ItemCustomListBinding.inflate(
                LayoutInflater.from(fragment.requireContext()),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProgress[position]
        holder.tvText.text = item

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_one)

        holder.itemView.setOnClickListener {
            if(fragment is AddFragment) {
                fragment.filterSelection(item)
            }
        }
    }

    fun show(listItems: List<String>) {
        listProgress = listItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listProgress.size
    }

}