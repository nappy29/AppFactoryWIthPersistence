package com.example.appfactorytest.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.databinding.SingleAlbumItemBinding

class AlbumAdapter(
    private val onClickListener: OnAlbumItemClickListener,
    private val onFavClicked: FavoriteButtonClickListener
) :
    PagingDataAdapter<Album, AlbumAdapter.AlbumViewHolder>(AlbumDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding: SingleAlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.single_album_item, parent, false
        )

        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class AlbumViewHolder(_binding: SingleAlbumItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        var binding: SingleAlbumItemBinding = _binding

        fun bind(album: Album) {
            binding.cardView.setOnClickListener {
                onClickListener.onClick(album)
            }

            binding.text.text = album.name

            if (album.imageList != null)
                album.imageList?.get(0)?.let { loadImageView(binding.image, it.url) }
            else
                loadImageView(binding.image, album.image_url)


            album.isLocal.let {
                binding.fav.isChecked = it
            }

            binding.fav.setOnCheckedChangeListener { _, isChecked ->
                    album.isLocal = !isChecked

                onFavClicked.onClick(album.isLocal, album)
            }


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

    class OnAlbumItemClickListener(val clickListener: (album: Album) -> Unit) {
        fun onClick(album: Album) = clickListener(album)
    }

    class FavoriteButtonClickListener(val clickListener: (isLocal: Boolean, album: Album) -> Unit) {
        fun onClick(isLocal: Boolean, album: Album) = clickListener(isLocal, album)
    }
}