package com.example.rickandmortyvs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyvs.adapters.episodes.EpisodesAdapter
import com.example.rickandmortyvs.databinding.FragmentEpisodesBinding
import com.example.rickandmortyvs.domain.models.episodes.EpisodeSearchOptions
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.episode.EpisodesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodesFragment : Fragment() {
    private val binding by viewBinding(FragmentEpisodesBinding::inflate)

    private val viewModel: EpisodesViewModel by viewModels()

    private val adapter = EpisodesAdapter {
        findNavController().navigate(
            EpisodesFragmentDirections.actionEpisodesFragmentToEpisodesDetailsFragment(
                it.id
            )
        )
    }

    private lateinit var searchBottomSheetBehaviour: BottomSheetBehavior<LinearLayout>

    private var episodesSearchOptions: EpisodeSearchOptions = EpisodeSearchOptions.NAME


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchAndFilterParamsCleaning()
        uiInitialization()
        searchBottomSheetSetup()
        swipeToRefreshSetup()
        searchSetup()
        getData()

        return binding.root
    }


    private fun searchAndFilterParamsCleaning() {
        viewModel.clearSearch()
    }

    private fun getData() {
        with(binding) {
            adapter.addLoadStateListener { combinedLoadStates ->
                if (combinedLoadStates.refresh is LoadState.Loading) {
                    progressBar.visibility = View.VISIBLE
                    placeHolderTextView.visibility = View.GONE
                } else if (adapter.itemCount == 0) {
                    progressBar.visibility = View.GONE
                    placeHolderTextView.visibility = View.VISIBLE
                    verticalRecyclerView.visibility = View.GONE
                } else if (combinedLoadStates.append is LoadState.Error || combinedLoadStates.refresh is LoadState.Error) {
                    progressBar.visibility = View.GONE
                    placeHolderTextView.visibility = View.VISIBLE
                    verticalRecyclerView.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    verticalRecyclerView.visibility = View.VISIBLE
                    placeHolderTextView.visibility = View.GONE
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getEpisodesList().collectLatest {
                    adapter.submitData(it)
                }

            }
        }
    }

    private fun searchBottomSheetSetup() {
        searchBottomSheetBehaviour = BottomSheetBehavior.from(binding.searchBottomSheet)
        searchBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

        with(binding) {
            searchParams.setOnClickListener {
                searchBottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
                searchRadioGroup.check(
                    when (episodesSearchOptions) {
                        EpisodeSearchOptions.NAME -> binding.nameSearchButton.id
                        EpisodeSearchOptions.EPISODE -> binding.episodeCodeSearchButton.id
                        else -> -1
                    }
                )
            }

            searchParamsApplyButton.setOnClickListener {
                when (searchRadioGroup.checkedRadioButtonId) {
                    nameSearchButton.id -> episodesSearchOptions = EpisodeSearchOptions.NAME
                    episodeCodeSearchButton.id -> episodesSearchOptions =
                        EpisodeSearchOptions.EPISODE
                }

                searchSetup()
                searchAndFilterParamsCleaning()

                searchBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

                searchClearButton.setOnClickListener {
                    episodesSearchOptions = EpisodeSearchOptions.NAME

                    searchSetup()

                    searchBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

                    searchAndFilterParamsCleaning()
                }

            }
        }

    }

    private fun swipeToRefreshSetup() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun searchSetup() {
        with(binding) {
            searchButton.setOnClickListener {
                val searchText = searchField.text.toString().lowercase()
                if (searchText.isNotEmpty()) {
                    viewModel.setSearchParameters(episodesSearchOptions, searchText)
                } else {
                    viewModel.setSearchParameters(episodesSearchOptions, "")
                }
            }
        }

    }

    private fun uiInitialization() {
        binding.verticalRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), COLUMNS_COUNT_VERTICAL)
        binding.verticalRecyclerView.adapter = adapter
        binding.placeHolderTextView.visibility = View.GONE
    }

    companion object {
        private const val COLUMNS_COUNT_VERTICAL = 2
    }


}