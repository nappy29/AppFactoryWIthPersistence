package com.example.appfactorytest.ui.mainPage

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.appfactorytest.R
import com.example.appfactorytest.databinding.MainFragmentPageBinding
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.ui.ArtistSearchFragment
import com.example.appfactorytest.ui.adapter.AlbumAdapter
import com.example.appfactorytest.ui.base.MainActivity
import com.example.appfactorytest.viewmodel.MainViewModel


@AndroidEntryPoint
class MainFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: MainFragmentPageBinding
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setSupportActionBar(binding.toolbar)

        albumAdapter = AlbumAdapter(AlbumAdapter.OnAlbumItemClickListener{ onAlbumItemclick(it)})

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
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

                else -> {super.onOptionsItemSelected(it)}
            }
        }

    }

    private fun setUpObservers(){
        viewModel.localAlbumTracks.observe(viewLifecycleOwner, {
            albumAdapter.submitData(lifecycle, PagingData.from(it))
        })
    }

    fun onAlbumItemclick(album: Album){
        Log.e("onclick", album.name)
        viewModel.getAlbumWithTracksById(album.album_id)

        viewModel.currentArtistName = null
        findNavController().navigate(R.id.action_mainFragment_to_detailsFragment)
    }
}