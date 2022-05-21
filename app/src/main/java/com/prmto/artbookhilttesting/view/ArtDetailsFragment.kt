package com.prmto.artbookhilttesting.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.databinding.FragmentArtDetailsBinding
import com.prmto.artbookhilttesting.util.Resource
import com.prmto.artbookhilttesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_art_details) {

    private var binding: FragmentArtDetailsBinding? = null
    lateinit var  viewModel: ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ArtViewModel::class.java]

        binding = FragmentArtDetailsBinding.bind(view)

        binding!!.artImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }

        subscribeToObservers()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding!!.btnSave.setOnClickListener {
            viewModel.makeArt(
                binding!!.nameText.text.toString(), binding!!.artistName.text.toString(),
                binding!!.yearText.text.toString()
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.selectedImage.observe(viewLifecycleOwner, Observer { url ->
            glide.load(url).into(binding!!.artImageView)
        })

        viewModel.insertArtMsg.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message?:"Error", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}