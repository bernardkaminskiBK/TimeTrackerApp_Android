package com.berni.timetrackerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.berni.timetrackerapp.R
import com.berni.timetrackerapp.databinding.ItemOverviewLayoutBinding
import com.berni.timetrackerapp.model.entities.Record
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class OverviewAdapter(val fragment: Fragment) :
    ListAdapter<Record, OverviewAdapter.ViewHolder>(RecordCallback()) {

    private var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onRecordClick(cardView: View, record: Record)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class ViewHolder(view: ItemOverviewLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        var image = view.ivOverviewImage
        var title = view.tvOverviewTitle
        var cardView = view.cvOverview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemOverviewLayoutBinding =
            ItemOverviewLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = getItem(position)

        Glide.with(holder.image)
            .load(record.imgUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)

        holder.cardView.transitionName =
            fragment.getString(R.string.record_card_transition_name, record.id)
        holder.title.text = record.name
        holder.itemView.setOnClickListener {
            onClickListener!!.onRecordClick(holder.cardView, record)
        }

    }

    class RecordCallback : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Record, newItem: Record) =
            oldItem == newItem
    }

}