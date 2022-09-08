package com.example.appfactorytest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appfactorytest.R
import com.example.appfactorytest.databinding.ArtistListFragmentBinding
import com.example.appfactorytest.databinding.DetailsFragmentBinding
import com.example.appfactorytest.ui.adapter.ArtistAdapter
import com.example.appfactorytest.ui.adapter.TrackAdapter
import com.example.appfactorytest.ui.adapter.loadImageView
import com.example.appfactorytest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: DetailsFragmentBinding
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false)
//        binding.lifecycleOwner = this
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
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))

    }

    private fun setUpObservers() {
        viewModel.albumAndTrackObject.observe(viewLifecycleOwner, {
            if(it != null){
                binding.albumWithTracks = it

                Log.d("DetailsFrg", it.album.image_url)
                binding.toolbar.title = it.album?.name

                onLoadIM(it.album.image_url)

                var string = "${it.album.name}, Contains ${it.tracks?.size?: 0} track(s)"
                binding.text.text = string

                Log.d("DetailsFrg", string)

                trackAdapter.submitList(it.tracks)
            }
        })
    }

    private fun onLoadIM(url: String){
        Glide.with(binding.image).load(url).
        error(R.drawable.ic_launcher_background).
        placeholder(R.drawable.ic_launcher_background).into(binding.image)
    }
}