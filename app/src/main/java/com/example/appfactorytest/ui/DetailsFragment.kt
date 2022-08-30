package com.example.appfactorytest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfactorytest.R
import com.example.appfactorytest.databinding.ArtistListFragmentBinding
import com.example.appfactorytest.databinding.DetailsFragmentBinding
import com.example.appfactorytest.ui.adapter.ArtistAdapter
import com.example.appfactorytest.ui.adapter.TrackAdapter
import com.example.appfactorytest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: DetailsFragmentBinding
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            itemAnimator = DefaultItemAnimator()
            adapter = trackAdapter
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.singleAlbumObject.observe(viewLifecycleOwner, {
            binding.albumWithTracks = it

            trackAdapter.submitList(it.tracks)
        })
    }
}