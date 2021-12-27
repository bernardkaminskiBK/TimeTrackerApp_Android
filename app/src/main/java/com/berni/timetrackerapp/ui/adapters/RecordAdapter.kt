package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.ItemRecordBinding
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.ui.fragments.records.RecordsFragment
import com.berni.timetrackerapp.utils.Formatter

class RecordAdapter(private val fragment: Fragment) :
    ListAdapter<Record, RecordAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: ItemRecordBinding) : RecyclerView.ViewHolder(view.root) {
        var date = view.tvDate
        var name = view.tvName
        var time = view.tvTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordBinding =
            ItemRecordBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = getItem(position)

        holder.date.text = record.createdDateFormatted
        holder.name.text = record.name
        holder.time.text = record.time

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_three)

        holder.itemView.setOnClickListener {
            if(fragment is RecordsFragment) {
                fragment.editRecord(record)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Record, newItem: Record) =
            oldItem == newItem
    }

}

