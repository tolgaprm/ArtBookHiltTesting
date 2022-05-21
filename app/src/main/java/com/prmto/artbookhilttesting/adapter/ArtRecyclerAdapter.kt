package com.prmto.artbookhilttesting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.prmto.artbookhilttesting.databinding.ArtRowBinding
import com.prmto.artbookhilttesting.roomdb.Art
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide: RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Art>() {
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var arts: List<Art>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    class ArtViewHolder(val binding: ArtRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val binding = ArtRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val art = arts[position]
        holder.binding.artRowNameText.text = "Name: ${art.name}"
        holder.binding.artRowArtistNameText.text = "Artist Name: ${art.artistName}"
        holder.binding.artRowYearText.text = "Year: ${art.year}"
        glide.load(art.imageUrl).into(holder.binding.artRowImageView)
    }

    override fun getItemCount(): Int {
        return arts.size
    }
}