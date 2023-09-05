package com.example.mywithingsapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mywithingsapp.R
import com.example.mywithingsapp.commons.communicator.HitsItem
import com.example.mywithingsapp.databinding.AdapterSearchBinding
import com.example.mywithingsapp.domain.entities.Image

class SearchAdapter :
    PagedListAdapter<Image, SearchAdapter.MyViewHolder>(PixabayItemDiffCallback()) {
    private var onHitsItemClickListener: HitsItem? = null
    private var onButtonClickListener: HitsItem? = null

    // OVERRIDE ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: AdapterSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context)
            , R.layout.adapter_search, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onHitsItemClickListener) }
        getItem(position)?.let { holder.bind(it, onButtonClickListener) }
    }

    override fun getItemViewType(position: Int): Int = position

    fun setOnHitsItemClickListener(onHitsItemClickListener: HitsItem) {
        this.onHitsItemClickListener = onHitsItemClickListener
    }

    fun setOnButtonClickListener(onButtonClickListener: HitsItem) {
        this.onButtonClickListener = onButtonClickListener
    }

    inner class MyViewHolder(private val binding: AdapterSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image, clickListener: HitsItem?) {
            binding.hitsList = image
            itemView.setOnClickListener {
                clickListener?.onHitsItemClickListener(image, it)
            }
            binding.buttonInfo.setOnClickListener { clickListener?.onButtonClickListener(image) }
        }
    }
}

class PixabayItemDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem == newItem
}
