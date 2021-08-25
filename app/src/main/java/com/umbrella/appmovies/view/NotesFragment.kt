package com.umbrella.appmovies.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentNotesBinding
import com.umbrella.appmovies.view.adapters.NotesAdapter
import com.umbrella.appmovies.viewmodel.MainViewModel

class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val notesAdapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notesRecyclerView.adapter = notesAdapter
        viewModel.getDatabaseNotesLiveData().observe(viewLifecycleOwner) {
            notesAdapter.setNotes(it)
        }
        notesAdapter.setOnNoteLongClickListener {
            viewModel.deleteNoteFromDB(it)
        }
        binding.buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.addNoteFragment)
        }
    }
}