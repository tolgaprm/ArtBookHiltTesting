package com.prmto.artbookhilttesting.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.adapter.ArtRecyclerAdapter
import com.prmto.artbookhilttesting.databinding.FragmentArtsBinding
import com.prmto.artbookhilttesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EarlyEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtFragment @Inject constructor(
    val artAdapter: ArtRecyclerAdapter
) : Fragment(R.layout.fragment_arts) {

    private var binding: FragmentArtsBinding? = null
    val viewModel: ArtViewModel by viewModels()

    private val swipeCallBack =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val layoutPosition = viewHolder.layoutPosition
                val selectedArt = artAdapter.arts[layoutPosition]
                viewModel.deleteArt(selectedArt)
            }

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArtsBinding.bind(view)


        subscribeToObservers()

        binding.recyclerViewArt.adapter = artAdapter
        binding.recyclerViewArt.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewArt)

        binding.fab.setOnClickListener {
            findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())
        }
    }

    private fun subscribeToObservers() {
        viewModel.artList.observe(viewLifecycleOwner, Observer {
            artAdapter.arts = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}

