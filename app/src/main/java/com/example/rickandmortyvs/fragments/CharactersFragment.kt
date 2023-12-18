package com.example.rickandmortyvs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmortyvs.adapters.CharactersAdapter
import com.example.rickandmortyvs.databinding.FragmentCharactersBinding
import com.example.rickandmortyvs.fragments.delegates.viewBinding
import com.example.rickandmortyvs.viewmodels.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {
    private val binding by viewBinding(FragmentCharactersBinding::inflate)

    private val viewModel: CharactersViewModel by viewModels()

    private val adapter = CharactersAdapter{

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return binding.root
    }


    fun getData(){
        with(binding) {
            verticalRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getCharactersList().collectLatest {
                    adapter.submitData(it)
                }

            }

            adapter.addLoadStateListener { combinedLoadStates ->
                if (combinedLoadStates.append is LoadState.Loading || combinedLoadStates.refresh is LoadState.Loading){
                    progressBar.visibility = View.VISIBLE
                    placeHolderTextView.visibility = View.GONE
                }
                else if (adapter.itemCount == 0){
                    placeHolderTextView.visibility = View.VISIBLE
                    verticalRecyclerView.visibility = View.GONE
                }
                else if (combinedLoadStates.append is LoadState.Error || combinedLoadStates.refresh is LoadState.Error){
                    placeHolderTextView.visibility = View.VISIBLE
                    verticalRecyclerView.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Ups...Something goes wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}