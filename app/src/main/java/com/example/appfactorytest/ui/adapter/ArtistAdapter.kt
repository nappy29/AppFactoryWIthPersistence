package com.example.appfactorytest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.databinding.SingleAlbumItemBinding
import com.example.appfactorytest.databinding.SingleArtistItemBinding

class ArtistAdapter(private val onClickListener: OnClickListener): PagingDataAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding: SingleArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.single_artist_item, parent, false
        )

        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ArtistViewHolder(_binding: SingleArtistItemBinding) : RecyclerView.ViewHolder(_binding.root){

        var binding: SingleArtistItemBinding = _binding

        fun bind(artist: Artist) {
            binding.cardView.setOnClickListener {
                onClickListener.onClick(artist)
            }
            binding.artist = artist
            binding.executePendingBindings()
        }
    }

    class ArtistDiffUtil : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (artist: Artist) -> Unit) {
        fun onClick(artist: Artist) = clickListener(artist)
    }
}