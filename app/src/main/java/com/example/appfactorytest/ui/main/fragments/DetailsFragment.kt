package com.example.appfactorytest.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfactorytest.R
import com.example.appfactorytest.databinding.DetailsFragmentBinding
import com.example.appfactorytest.ui.main.adapter.TrackAdapter
import com.example.appfactorytest.ui.main.adapter.loadImageView
import com.example.appfactorytest.util.Status
import com.example.appfactorytest.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: DetailsFragmentBinding
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        viewModel.albumAndTrackSearchResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> binding.progressCircular.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.albumAndTrackObject.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.progressCircular.visibility = View.GONE
                binding.albumWithTracks = it

                binding.toolbar.title = it.album?.name

                loadImageView(binding.image, it.album.image_url)

                var string = "${it.album.name}, Contains ${it.tracks?.size ?: 0} track(s)"
                binding.text.text = string

                trackAdapter.submitList(it.tracks)
            }
        })

        viewModel._connectivity_res.observe(viewLifecycleOwner, {
            if (it == false)
                Toast.makeText(requireContext(), "You are Offline", Toast.LENGTH_LONG).show()
        })
    }


}