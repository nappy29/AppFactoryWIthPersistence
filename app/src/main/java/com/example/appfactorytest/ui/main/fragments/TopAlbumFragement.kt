package com.example.appfactorytest.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Album
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.databinding.AlbumListFragmentBinding
import com.example.appfactorytest.ui.main.adapter.AlbumAdapter
import com.example.appfactorytest.ui.main.adapter.LoadStateAdapter
import com.example.appfactorytest.ui.activity.MainActivity
import com.example.appfactorytest.util.Status
import com.example.appfactorytest.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TopAlbumFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: AlbumListFragmentBinding
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var artist: Artist

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        albumAdapter = AlbumAdapter(AlbumAdapter.OnAlbumItemClickListener {
            onAlbumItemclick(it)
        }, AlbumAdapter.FavoriteButtonClickListener { isLocal, album ->
            favoriteClicked(isLocal, album)
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
        viewModel.statusLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> binding.progressCircular.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.singleArtistObject.observe(viewLifecycleOwner, {
            if (it != null) {
                artist = it
                binding.toolbar.title = "${it.name}'s Top Albums"
                Artist_name = it.name

                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    viewModel.topAlbumSearch(Artist_name)?.observe(viewLifecycleOwner, {
                        albumAdapter.submitData(lifecycle, it)
                    })
                }

            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem: MenuItem = menu.findItem(R.id.search)
        menuItem.setVisible(false)
    }

    fun onAlbumItemclick(album: Album) {
        viewModel.setalbumithTracksObject(null)
        viewModel.getAlbumTracks(artist.name, album)
        findNavController().navigate(R.id.action_topAlbumFragment_to_detailsFragment)
    }

    fun favoriteClicked(isLocal: Boolean, album: Album) {
        if (isLocal)
            viewModel.deleteAlbum(album)
        else
            viewModel.getAlbumTracks(Artist_name, album)
    }

    companion object {
        var Artist_name = ""
    }
}