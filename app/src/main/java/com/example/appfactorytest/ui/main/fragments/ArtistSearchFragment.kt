package com.example.appfactorytest.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfactorytest.R
import com.example.appfactorytest.data.model.Artist
import com.example.appfactorytest.databinding.ArtistListFragmentBinding
import com.example.appfactorytest.ui.main.adapter.ArtistAdapter
import com.example.appfactorytest.ui.main.adapter.LoadStateAdapter
import com.example.appfactorytest.util.Status
import com.example.appfactorytest.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistSearchFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: ArtistListFragmentBinding
    private lateinit var artistAdapter: ArtistAdapter

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.artist_list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        artistAdapter = ArtistAdapter(ArtistAdapter.OnClickListener {
            onArtistItemclick(it)
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            itemAnimator = DefaultItemAnimator()
            adapter = artistAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter(retry = { setUpObserver(Search_Name) }),
                footer = LoadStateAdapter(retry = { setUpObserver(Search_Name) })
            )
        }


        setUpToolbar()
        setUpSearchView()

        if (!Search_Name.isEmpty())
            setUpObserver(Search_Name)

        binding.searchBtn.setOnClickListener {
            var searchTxt = binding.searchView.query.toString()
            setUpObserver(searchTxt)
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setIconifiedByDefault(false)
    }

    private fun setUpObserver(artistName: String) {

        Search_Name = artistName

        viewModel.statusLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> binding.progressCircular.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchByArtist(Search_Name).observe(viewLifecycleOwner, {
                artistAdapter.submitData(this@ArtistSearchFragment.lifecycle, it)
            })
        }

    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.title = "Search Artist"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

    }

    fun onArtistItemclick(artist: Artist) {
        viewModel.setArtistObject(artist)
        findNavController().navigate(R.id.action_artistListFragment_to_topAlbumFragment2)
    }

    companion object {
        var Search_Name = ""
    }

}