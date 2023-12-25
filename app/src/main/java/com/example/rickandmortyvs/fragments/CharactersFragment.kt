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
import com.example.rickandmortyvs.adapters.CharactersAdapter
import com.example.rickandmortyvs.databinding.FragmentCharactersBinding
import com.example.rickandmortyvs.domain.models.CharactersSearchOptions
import com.example.rickandmortyvs.domain.models.Gender
import com.example.rickandmortyvs.domain.models.Status
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.CharactersViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private val binding by viewBinding(FragmentCharactersBinding::inflate)

    private val viewModel: CharactersViewModel by viewModels()

    private val adapter = CharactersAdapter {
        findNavController().navigate(CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(it.id))
    }

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<LinearLayout>
    private lateinit var searchBottomSheetBehaviour: BottomSheetBehavior<LinearLayout>

    private var charactersSearchOptions: CharactersSearchOptions = CharactersSearchOptions.NAME
    private var status: Status = Status.EMPTY
    private var gender: Gender = Gender.EMPTY


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchAndFilterParamsCleaning()
        binding.verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.verticalRecyclerView.adapter = adapter
        binding.placeHolderTextView.visibility = View.GONE
        bottomSheetSetup()
        searchBottomSheetSetup()
        swipeToRefreshSetup()
        searchSetup()
        getData()


        return binding.root
    }


    private fun searchAndFilterParamsCleaning() {
        viewModel.clearSearch()
        viewModel.removeFilters()
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
                        "Ups...Something goes wrong1",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    verticalRecyclerView.visibility = View.VISIBLE
                    placeHolderTextView.visibility = View.GONE
                }
            }


            //verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getCharactersList().collectLatest {
                    adapter.submitData(it)
                }

            }
        }
    }

    private fun bottomSheetSetup() {
        bottomSheetBehaviour = BottomSheetBehavior.from(binding.filtersBottomSheet)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

        with(binding) {
            filterButton.setOnClickListener {
                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED

                binding.statusRadioGroup.check(
                    when (status) {
                        Status.ALIVE -> binding.StatusAliveButton.id
                        Status.DEAD -> binding.StatusDeadButton.id
                        Status.UNKNOWN -> binding.StatusUnknownButton.id
                        else -> -1
                    }
                )

                binding.genderRadioGroup.check(
                    when (gender) {
                        Gender.FEMALE -> binding.GenderFemaleButton.id
                        Gender.MALE -> binding.GenderMaleButton.id
                        Gender.GENDERLESS -> binding.GenderGenderlessButton.id
                        Gender.UNKNOWN -> binding.GenderUnknownButton.id
                        else -> -1
                    }
                )

                binding.filterApplyButton.setOnClickListener {
                    when (binding.statusRadioGroup.checkedRadioButtonId) {
                        binding.StatusAliveButton.id -> status = Status.ALIVE
                        binding.StatusDeadButton.id -> status = Status.DEAD
                        binding.StatusUnknownButton.id -> status = Status.UNKNOWN
                    }

                    when (binding.genderRadioGroup.checkedRadioButtonId) {
                        binding.GenderFemaleButton.id -> gender = Gender.FEMALE
                        binding.GenderMaleButton.id -> gender = Gender.MALE
                        binding.GenderGenderlessButton.id -> gender = Gender.GENDERLESS
                        binding.GenderUnknownButton.id -> gender = Gender.UNKNOWN
                    }

                    viewModel.addGenderFilter(gender)
                    viewModel.addStatusFilter(status)

                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

                }

                binding.filterClearButton.setOnClickListener {
                    status = Status.EMPTY
                    gender = Gender.EMPTY
                    viewModel.addGenderFilter(gender)
                    viewModel.addStatusFilter(status)
                    binding.statusRadioGroup.clearCheck()
                    binding.genderRadioGroup.clearCheck()

                    bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
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
                    when (charactersSearchOptions) {
                        CharactersSearchOptions.NAME -> binding.nameSearchButton.id
                        CharactersSearchOptions.SPECIES -> binding.speciesSearchButton.id
                        CharactersSearchOptions.TYPE -> binding.typeSearchButton.id
                        else -> -1
                    }
                )
            }

            searchParamsApplyButton.setOnClickListener {
                when (searchRadioGroup.checkedRadioButtonId) {
                    nameSearchButton.id -> charactersSearchOptions = CharactersSearchOptions.NAME
                    speciesSearchButton.id -> charactersSearchOptions =
                        CharactersSearchOptions.SPECIES

                    typeSearchButton.id -> charactersSearchOptions = CharactersSearchOptions.TYPE
                }

                searchSetup()
                searchAndFilterParamsCleaning()

                searchBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

                searchClearButton.setOnClickListener {
                    charactersSearchOptions = CharactersSearchOptions.NAME

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
                    viewModel.setSearchParameters(charactersSearchOptions, searchText)
                } else {
                    viewModel.setSearchParameters(charactersSearchOptions, "")
                }
            }
        }

    }


}