package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.databinding.ItemProgressBinding
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.utils.Formatter

class TimeTrackerAdapter(private val fragment: Fragment) :
    ListAdapter<Progress, TimeTrackerAdapter.ViewHolder>(DiffCallback()) {

    private var progresses: List<Progress> = listOf()

    class ViewHolder(view: ItemProgressBinding) : RecyclerView.ViewHolder(view.root) {
        var date = view.tvDate
        var name = view.tvName
        var time = view.tvTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemProgressBinding =
            ItemProgressBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val progress = progresses[position]

        holder.date.text = Formatter.dateFormat(progress.date)
        holder.name.text = progress.name
        holder.time.text = progress.time
    }

    override fun getItemCount(): Int {
        return progresses.size
    }

    fun show(list: List<Progress>) {
        progresses = list
        notifyDataSetChanged()
    }

    class DiffCallback : DiffUtil.ItemCallback<Progress>() {
        override fun areItemsTheSame(oldItem: Progress, newItem: Progress) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Progress, newItem: Progress) =
            oldItem == newItem
    }

}

