package com.prmto.artbookhilttesting.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.adapter.ImageRecyclerAdapter
import com.prmto.artbookhilttesting.databinding.FragmentImageApiBinding
import com.prmto.artbookhilttesting.util.Resource
import com.prmto.artbookhilttesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageApiFragment @Inject constructor(
    val imageRecyclerAdapter: ImageRecyclerAdapter
) : Fragment(R.layout.fragment_image_api) {

    lateinit var  viewModel: ArtViewModel

    private var binding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ArtViewModel::class.java]

        binding = FragmentImageApiBinding.bind(view)

        var job: Job? = null

        binding!!.editTextTextPersonName.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding!!.imageRecyclerView.adapter = imageRecyclerAdapter
        binding!!.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.image.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val urls = it.data?.hits?.map {
                        it.previewURL
                    }

                    imageRecyclerAdapter.images = urls ?: emptyList()
                    binding!!.progressBar.visibility = View.GONE
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_SHORT)
                        .show()
                    binding!!.progressBar.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding!!.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
