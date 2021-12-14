package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.ItemRecordFilterBinding
import com.berni.timetrackerapp.ui.fragments.records.RecordsFragment

class FilterAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    private lateinit var recordNames: List<String>

    class ViewHolder(view: ItemRecordFilterBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordFilterBinding =
            ItemRecordFilterBinding.inflate(
                LayoutInflater.from(fragment.requireContext()),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recordNames[position]
        holder.tvText.text = item

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_one)

        holder.itemView.setOnClickListener {
            if (fragment is RecordsFragment) {
                fragment.filterSelection(item)
            }
        }
    }

    fun show(listItems: List<String>) {
        recordNames = listItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return recordNames.size
    }

}