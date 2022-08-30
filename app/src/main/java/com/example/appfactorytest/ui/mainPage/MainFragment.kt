package com.example.appfactorytest.ui.mainPage

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.appfactorytest.R
import com.example.appfactorytest.databinding.MainFragmentPageBinding
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfactorytest.ui.adapter.AlbumAdapter
import com.example.appfactorytest.ui.base.MainActivity
import com.example.appfactorytest.viewmodel.MainViewModel


@AndroidEntryPoint
class MainFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
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

        albumAdapter = AlbumAdapter()
        setUpToolbar()

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = albumAdapter
        }


    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            // do something when click navigation
        }

        binding.toolbar.title = "Your Saved Albums"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_artistListFragment)
                    true
                }

                else -> {super.onOptionsItemSelected(it)}
            }
        }
    }

}