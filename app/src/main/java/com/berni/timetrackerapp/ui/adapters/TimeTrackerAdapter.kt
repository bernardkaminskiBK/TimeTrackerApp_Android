package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.ItemProgressBinding
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.ui.fragments.add.AddFragment
import com.berni.timetrackerapp.utils.Formatter
import java.util.*

class TimeTrackerAdapter(private val fragment: Fragment) :
    ListAdapter<Progress, TimeTrackerAdapter.ViewHolder>(DiffCallback()) {

    private var list = mutableListOf<Progress>()

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
        val progress = getItem(position)

        holder.date.text = Formatter.dateFormat(progress.date)
        holder.name.text = progress.name
        holder.time.text = progress.time

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animation_three)

        holder.itemView.setOnClickListener {
            if(fragment is AddFragment) {
                fragment.updateProgressItem(progress)
            }
        }
    }

    fun setData(list: MutableList<Progress>) {
        this.list = list
        submitList(list)
    }

    class DiffCallback : DiffUtil.ItemCallback<Progress>() {
        override fun areItemsTheSame(oldItem: Progress, newItem: Progress) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Progress, newItem: Progress) =
            oldItem == newItem
    }

   fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
          val filteredList = mutableListOf<Progress>()
            if(constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                for(item in list) {
                    if(item.name.lowercase(Locale.getDefault()).startsWith(constraint.toString()
                            .lowercase(Locale.getDefault()))) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
           submitList(results?.values as MutableList<Progress>)
        }

    }

}

