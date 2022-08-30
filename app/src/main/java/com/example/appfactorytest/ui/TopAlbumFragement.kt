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
import com.example.appfactorytest.databinding.AlbumListFragmentBinding
import com.example.appfactorytest.databinding.MainFragmentPageBinding
import com.example.appfactorytest.ui.adapter.AlbumAdapter
import com.example.appfactorytest.ui.base.MainActivity
import com.example.appfactorytest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.*


@AndroidEntryPoint
class TopAlbumFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: AlbumListFragmentBinding
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.album_list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setSupportActionBar(binding.toolbar)

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = albumAdapter
        }


    }
}