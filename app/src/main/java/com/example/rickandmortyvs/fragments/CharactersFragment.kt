package com.example.rickandmortyvs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rickandmortyvs.databinding.FragmentCharactersBinding
import com.example.rickandmortyvs.fragments.delegates.viewBinding

class CharactersFragment : Fragment() {
    private val binding by viewBinding(FragmentCharactersBinding::inflate)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return binding.root
    }
}