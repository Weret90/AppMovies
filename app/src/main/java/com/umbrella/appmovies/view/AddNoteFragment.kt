package com.umbrella.appmovies.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentAddNoteBinding
import com.umbrella.appmovies.model.Note
import com.umbrella.appmovies.viewmodel.MainViewModel

class AddNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddNoteIntoDB.setOnClickListener {
            with(binding) {
                val filmTitle = editTextFilmTitle.text.toString()
                val dateOfWatching = editTextDateOfWatching.text.toString()
                val review = editTextReview.text.toString()
                val note = Note(filmTitle, dateOfWatching, review)
                viewModel.insertNoteIntoDB(note)
                findNavController().navigate(R.id.notesFragment)
            }
        }
    }
}