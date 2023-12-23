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

    private val args by navArgs<LocationDetailsFragmentArgs>()

    var params: LocationDetails? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.checkNetworkAvailability(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            params = viewModel.getLocationDetails(args.id)
            with(binding){
                locationDetailsNameText.text = params?.name
                locationDetailsDimensionText.text = params?.dimension
                locationDetailsTypeText.text = params?.type
                toolbar.title = params?.name
            }

        }

        binding.toolbar.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }
}