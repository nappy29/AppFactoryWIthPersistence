package com.example.appfactorytest.ui

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.databinding.AlbumListFragmentBinding
import com.example.appfactorytest.ui.adapter.AlbumAdapter
import com.example.appfactorytest.ui.base.MainActivity
import com.example.appfactorytest.util.Status
import com.example.appfactorytest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import androidx.lifecycle.lifecycleScope
import com.example.appfactorytest.ui.adapter.LoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TopAlbumFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: AlbumListFragmentBinding
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var artist: Artist

    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.album_list_fragment, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setSupportActionBar(binding.toolbar)

        albumAdapter = AlbumAdapter(AlbumAdapter.OnAlbumItemClickListener{
            onAlbumItemclick(it)
        })
        binding.recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = albumAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter(retry = { setUpObservers() }),
                footer = LoadStateAdapter(retry = { setUpObservers() })
            )
        }

        setUpToolbar()
        setUpObservers()
    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

    }
        private fun setUpObservers() {
        viewModel.singleArtistObject.observe(viewLifecycleOwner, {
             if(it != null) {
                 artist=it
                 binding.toolbar.title = "${it.name}'s Top Albums"
//                 viewModel.topAlbumSearch(it.name)
                 Search_Name = it.name

                 searchJob?.cancel()
                 searchJob = lifecycleScope.launch {
                     viewModel.topAlbumSearch(Search_Name).observe(viewLifecycleOwner, {
                         albumAdapter.submitData(lifecycle, it)
                     })
                 }

             }
        })

//        viewModel.topAlbumSearchResponse.observe(viewLifecycleOwner, {
//            when(it.status){
//                Status.SUCCESS ->{
//                    it.data?.get(0)?.let { it1 -> Log.e("artist", it1.url) }
//                    albumAdapter.submitList(it.data)
//                }
//                Status.LOADING ->{
//
//                }
//                Status.ERROR ->{
//
//                }
//            }
//        })

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem: MenuItem = menu.findItem(R.id.search)
        menuItem.setVisible(false)
    }

    fun onAlbumItemclick(album: Album){
        viewModel.getAlbumTracks(artist.name, album)
//        Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.action_topAlbumFragment_to_detailsFragment)

        findNavController().navigate(R.id.action_topAlbumFragment_to_detailsFragment)
    }

    companion object{
        var Search_Name = ""
    }
}