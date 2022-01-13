package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.databinding.ItemOverviewLayoutBinding
import com.berni.timetrackerapp.model.entities.Record

class OverviewAdapter(val fragment: Fragment) :
    RecyclerView.Adapter<OverviewAdapter.ViewHolder>() {

    private var records: List<Record> = listOf()

    class ViewHolder(view: ItemOverviewLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        var title = view.tvOverviewTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemOverviewLayoutBinding =
            ItemOverviewLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]

        holder.title.text = record.name

    }

    override fun getItemCount(): Int {
        return records.size
    }

    fun recordsList(list: List<Record>) {
        records = list
        notifyDataSetChanged()
    }

}