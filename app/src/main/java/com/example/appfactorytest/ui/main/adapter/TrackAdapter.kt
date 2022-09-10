package com.example.appfactorytest.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Track
import com.example.appfactorytest.databinding.SingleTrackBinding

class TrackAdapter : ListAdapter<Track, TrackAdapter.TrackViewHolder>(TrackDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding: SingleTrackBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.single_track, parent, false
        )

        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TrackViewHolder(_binding: SingleTrackBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        var binding: SingleTrackBinding = _binding

        fun bind(track: Track) {
            binding.track = track
            binding.executePendingBindings()
        }
    }

    class TrackDiffUtil : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}