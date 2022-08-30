package com.example.appfactorytest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.databinding.ArtistListFragmentBinding
import com.example.appfactorytest.ui.adapter.ArtistAdapter
import com.example.appfactorytest.util.Status
import com.example.appfactorytest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistSearchFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ArtistListFragmentBinding
    private lateinit var artistAdapter: ArtistAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.artist_list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        artistAdapter = ArtistAdapter(ArtistAdapter.OnClickListener{
            onArtistItemclick(it)
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = artistAdapter
        }

        setUpToolbar()
        setUpSearchView()
        setUpObservers()

        binding.searchBtn.setOnClickListener{
            var searchTxt = binding.searchView.query.toString()
            viewModel.searchByArtist(searchTxt)
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setIconifiedByDefault(false)
    }

    private fun setUpObservers() {
        viewModel.artistSearchResponse.observe(viewLifecycleOwner, {
            when(it.status){
                Status.SUCCESS -> {
                    artistAdapter.submitList(it.data)
                    Log.d("searchfrag", it.data?.size.toString())
                }
                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
        })
    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            // do something when click navigation
        }

        binding.toolbar.title = "Search Artist"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

    }

    fun onArtistItemclick(artist: Artist){
        Log.e("onclick", artist.name)
        viewModel.topAlbumSearch(artist.name)
        Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.action_artistListFragment_to_topAlbumFragment2)
    }


}