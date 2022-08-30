package com.example.appfactorytest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.databinding.SingleAlbumItemBinding

class AlbumAdapter(): ListAdapter<Album, AlbumAdapter.AlbumViewHolder>(AlbumDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding: SingleAlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.single_album_item, parent, false
        )

        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumViewHolder(_binding: SingleAlbumItemBinding) : RecyclerView.ViewHolder(_binding.root){

        var binding: SingleAlbumItemBinding = _binding

        fun bind(album: Album) {
            binding.album = album
            binding.executePendingBindings()
        }
    }

    class AlbumDiffUtil : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}