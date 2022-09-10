package com.example.appfactorytest.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.databinding.MainFragmentPageBinding
import com.example.appfactorytest.ui.TopAlbumFragment
import com.example.appfactorytest.ui.main.adapter.AlbumAdapter
import com.example.appfactorytest.ui.activity.MainActivity
import com.example.appfactorytest.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: MainFragmentPageBinding
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setSupportActionBar(binding.toolbar)

        albumAdapter = AlbumAdapter(AlbumAdapter.OnAlbumItemClickListener { onAlbumItemclick(it) },
            AlbumAdapter.FavoriteButtonClickListener { isLocal, album ->
                favoriteClicked(isLocal, album)
            })

        binding.recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            itemAnimator = DefaultItemAnimator()
            adapter = albumAdapter
        }

        setUpToolbar()
        viewModel.getAllAlbumTracksLocally()
        setUpObservers()
    }

    private fun setUpToolbar() {

        binding.toolbar.title = "Your Saved Albums"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    findNavController().navigate(R.id.action_mainFragment_to_artistListFragment)
                    true
                }

                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        viewModel._connectivity_res.observe(viewLifecycleOwner, {
            if (it == false)
                Toast.makeText(requireContext(), "You are Offline", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(requireContext(), "You are Online", Toast.LENGTH_LONG).show()

        })

    }

    private fun setUpObservers() {
        viewModel.localAlbumTracks.observe(viewLifecycleOwner, {

            if(it.isEmpty())
                binding.textview.visibility = View.VISIBLE
            else
                binding.textview.visibility = View.GONE

            albumAdapter.submitData(lifecycle, PagingData.from(it))
        })
    }

    fun onAlbumItemclick(album: Album) {
        viewModel.getAlbumWithTracksById(album.album_id)

        viewModel.currentArtistName = null
        findNavController().navigate(R.id.action_mainFragment_to_detailsFragment)
    }

    fun favoriteClicked(isLocal: Boolean, album: Album) {
        if (isLocal)
            viewModel.deleteAlbum(album)
        else
            viewModel.getAlbumTracks(TopAlbumFragment.Artist_name, album)
    }
}