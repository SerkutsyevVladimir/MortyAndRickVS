package com.example.rickandmortyvs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyvs.adapters.locations.LocationsAdapter
import com.example.rickandmortyvs.databinding.FragmentLocationsBinding
import com.example.rickandmortyvs.domain.models.locations.LocationsSearchOptions
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.location.LocationsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment : Fragment() {
    private val binding by viewBinding(FragmentLocationsBinding::inflate)

    private val viewModel: LocationsViewModel by viewModels()

    private val adapter = LocationsAdapter {
        findNavController().navigate(LocationsFragmentDirections.actionLocationFragmentToLocationDetailsFragment(it.id))
    }

    private lateinit var searchBottomSheetBehaviour: BottomSheetBehavior<LinearLayout>

    private var locationsSearchOptions: LocationsSearchOptions = LocationsSearchOptions.NAME


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchAndFilterParamsCleaning()
        binding.verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.verticalRecyclerView.adapter = adapter
        binding.placeHolderTextView.visibility = View.GONE
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
                    placeHolderTextView.visibility = View.VISIBLE
                    verticalRecyclerView.visibility = View.GONE
                } else if (combinedLoadStates.append is LoadState.Error || combinedLoadStates.refresh is LoadState.Error) {
                    placeHolderTextView.visibility = View.VISIBLE
                    verticalRecyclerView.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Ups...Something goes wrong",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    verticalRecyclerView.visibility = View.VISIBLE
                    placeHolderTextView.visibility = View.GONE
                }
            }


            //verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getLocationDetailsList().collectLatest {
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
                    when (locationsSearchOptions) {
                        LocationsSearchOptions.NAME -> binding.nameSearchButton.id
                        LocationsSearchOptions.DIMENSION -> binding.dimensionSearchButton.id
                        LocationsSearchOptions.TYPE -> binding.typeSearchButton.id
                        else -> -1
                    }
                )
            }

            searchParamsApplyButton.setOnClickListener {
                when (searchRadioGroup.checkedRadioButtonId) {
                    nameSearchButton.id -> locationsSearchOptions = LocationsSearchOptions.NAME
                    dimensionSearchButton.id -> locationsSearchOptions =
                        LocationsSearchOptions.DIMENSION

                    typeSearchButton.id -> locationsSearchOptions = LocationsSearchOptions.TYPE
                }

                searchSetup()
                searchAndFilterParamsCleaning()

                searchBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

                searchClearButton.setOnClickListener {
                    locationsSearchOptions = LocationsSearchOptions.NAME

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
                    viewModel.setSearchParameters(locationsSearchOptions, searchText)
                } else {
                    viewModel.setSearchParameters(locationsSearchOptions, "")
                }
            }
        }

    }


}