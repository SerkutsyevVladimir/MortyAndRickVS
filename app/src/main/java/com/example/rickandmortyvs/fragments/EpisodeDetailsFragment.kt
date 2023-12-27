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
import com.example.rickandmortyvs.databinding.FragmentEpisodeDetailsBinding
import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.episode.EpisodeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment() {
    private val binding by viewBinding(FragmentEpisodeDetailsBinding::inflate)

    private val viewModel: EpisodeDetailsViewModel by viewModels()

    private val adapter = LocationsDetailsAdapter {
        findNavController().navigate(
            EpisodeDetailsFragmentDirections.actionEpisodeDetailsFragmentToCharacterDetailsFragment(
                it.id
            )
        )
    }

    private val args by navArgs<EpisodeDetailsFragmentArgs>()

    private var episode: Episode? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.checkNetworkAvailability(requireContext())
        uiInitialization()
        displayCharactersRecyclerView()
        binding.toolbar.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }

    private fun displayCharactersRecyclerView() {
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

    private fun uiInitialization(){
        viewLifecycleOwner.lifecycleScope.launch {
            episode = viewModel.getEpisodeDetails(args.id)
            with(binding) {
                episodeDetailsNameText.text = episode?.name
                episodeDetailsEpisodeCodeText.text = episode?.episode
                episodeDetailsAirDateText.text = episode?.airDate
                toolbar.title = episode?.name
            }
            viewModel.getMultipleCharacters(episode?.characters)

        }
    }
}