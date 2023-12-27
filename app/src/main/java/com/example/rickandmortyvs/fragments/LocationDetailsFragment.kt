package com.example.rickandmortyvs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyvs.adapters.locations.LocationsDetailsAdapter
import com.example.rickandmortyvs.databinding.FragmentLocationDetailsBinding
import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.location.LocationDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    private val binding by viewBinding(FragmentLocationDetailsBinding::inflate)

    private val viewModel: LocationDetailsViewModel by viewModels()

    private val adapter = LocationsDetailsAdapter {
        findNavController().navigate(
            LocationDetailsFragmentDirections.actionLocationDetailsFragmentToCharacterDetailsFragment(
                it.id
            )
        )
    }

    private val args by navArgs<LocationDetailsFragmentArgs>()

    private var locationDetails: LocationDetails? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.checkNetworkAvailability(requireContext())
        uiInitialization()
        displayEpisodesRecyclerView()
        binding.toolbar.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }

    private fun displayEpisodesRecyclerView() {
        with(binding) {
            verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            viewLifecycleOwner.lifecycleScope.launch {
                verticalRecyclerView.adapter = adapter

                viewModel.charactersStateFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun uiInitialization() {
        viewLifecycleOwner.lifecycleScope.launch {
            locationDetails = viewModel.getLocationDetails(args.id)
            with(binding) {
                locationDetailsNameText.text = locationDetails?.name
                locationDetailsDimensionText.text = locationDetails?.dimension
                locationDetailsTypeText.text = locationDetails?.type
                toolbar.title = locationDetails?.name
            }
            viewModel.getMultipleCharacters(locationDetails?.residents)

        }
    }
}