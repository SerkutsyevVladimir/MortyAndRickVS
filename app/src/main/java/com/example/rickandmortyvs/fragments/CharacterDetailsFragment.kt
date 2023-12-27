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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rickandmortyvs.adapters.CharactersDetailsAdapter
import com.example.rickandmortyvs.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {
    private val binding by viewBinding(FragmentCharacterDetailsBinding::inflate)

    private val viewModel: CharacterDetailsViewModel by viewModels()

    private val adapter = CharactersDetailsAdapter {
        findNavController().navigate(
            CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToEpisodesDetailsFragment(
                it.id
            )
        )
    }

    private val args by navArgs<CharacterDetailsFragmentArgs>()

    var character: Characters? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.checkNetworkAvailability(requireContext())
        uiInitialization()
        displayEpisodesRecyclerView()
        binding.toolbar.setOnClickListener { findNavController().popBackStack() }
        redirectionToLocationDetails(character)
        redirectionToOriginDetails(character)

        return binding.root
    }

    private fun displayEpisodesRecyclerView() {
        with(binding) {
            verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            viewLifecycleOwner.lifecycleScope.launch {
                verticalRecyclerView.adapter = adapter

                viewModel.episodeStateFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun redirectionToLocationDetails(params: Characters?) {
        val paramsUrl = params?.location?.url
        if (!paramsUrl.isNullOrEmpty() && paramsUrl.isNotBlank()) {
            val locationId = params.location.url.split("/").last().toInt()
            binding.charactersDetailsLocationText.setOnClickListener {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToLocationDetailsFragment(
                        locationId
                    )
                )
            }
        }
    }

    private fun redirectionToOriginDetails(params: Characters?) {
        val paramsUrl = params?.origin?.url
        if (!paramsUrl.isNullOrEmpty() && paramsUrl.isNotBlank()) {
            val originId = params.origin.url.split("/").last().toInt()
            binding.charactersDetailsOriginText.setOnClickListener {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToLocationDetailsFragment(
                        originId
                    )
                )
            }
        }

    }

    private fun uiInitialization() {
        viewLifecycleOwner.lifecycleScope.launch {
            character = viewModel.getCharacter(args.id)
            with(binding) {
                charactersDetailsNameText.text = character?.name
                charactersDetailsSpeciesText.text = character?.species
                charactersDetailsGenderText.text = character?.gender
                charactersDetailsStatusText.text = character?.status
                charactersDetailsTypeText.text = character?.type
                charactersDetailsOriginText.text = character?.origin?.name
                charactersDetailsLocationText.text = character?.location?.name
                Glide.with(charactersDetailsImageView.context)
                    .load(character?.image)
                    .into(charactersDetailsImageView)
                toolbar.title = character?.name
            }
            viewModel.getMultipleEpisodes(character?.episode)

            redirectionToOriginDetails(character)
            redirectionToLocationDetails(character)
        }
    }


}