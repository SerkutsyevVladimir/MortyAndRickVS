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
        findNavController().navigate(CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToEpisodesDetailsFragment(it.id))
    }

    private val args by navArgs<CharacterDetailsFragmentArgs>()

     var params: Characters? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.checkNetworkAvailability(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
           params = viewModel.getCharacter(args.id)
            with(binding){
                charactersDetailsNameText.text = params?.name
                charactersDetailsSpeciesText.text = params?.species
                charactersDetailsGenderText.text = params?.gender
                charactersDetailsStatusText.text = params?.status
                charactersDetailsTypeText.text = params?.type
                charactersDetailsOriginText.text = params?.origin?.name
                charactersDetailsLocationText.text = params?.location?.name
                Glide.with(charactersDetailsImageView.context)
                    .load(params?.image)
                    .into(charactersDetailsImageView)
                toolbar.title = params?.name
            }
            viewModel.getMultipleEpisodes(params?.episode)
            redirectionToLocationDetails(params)
            redirectionToOriginDetails(params)

        }
        displayEpisodesRecyclerView()


        binding.toolbar.setOnClickListener { findNavController().popBackStack() }
        redirectionToLocationDetails(params)
        redirectionToOriginDetails(params)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun displayEpisodesRecyclerView(){
        with(binding){
            verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            viewLifecycleOwner.lifecycleScope.launch {
                verticalRecyclerView.adapter=adapter

                viewModel.episodeStateFlow.collect {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun redirectionToLocationDetails(params: Characters?){
        val locationId = params?.location?.url?.split("/")?.last()?.toInt()
        if (locationId != null) {
            binding.charactersDetailsLocationText.setOnClickListener {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToLocationDetailsFragment(
                        locationId
                    )
                )
            }
        }
    }

    private fun redirectionToOriginDetails(params: Characters?){
        val originId = params?.origin?.url?.split("/")?.last()?.toInt()
        if (originId != null) {
            binding.charactersDetailsOriginText.setOnClickListener {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToLocationDetailsFragment(
                        originId
                    )
                )
            }
        }
    }


}