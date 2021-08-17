package com.umbrella.appmovies.view.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentSelectedFilmsBinding
import com.umbrella.appmovies.view.adapters.FilmsAdapter
import com.umbrella.appmovies.viewmodel.MainViewModel

class SelectedFilmsFragment : Fragment() {

    private var _binding: FragmentSelectedFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FilmsAdapter()
        binding.selectedFilms.adapter = adapter
        adapter.setOnFilmClickListener { film ->
            val bundle = Bundle()
            bundle.putSerializable(FilmsFragment.ARG_FILM, film)
            findNavController().navigate(R.id.filmDetailFragment, bundle)
        }
        adapter.setOnFilmLongClickListener { film ->
            viewModel.deleteFilmFromDB(film)
            Toast.makeText(
                context,
                binding.root.resources.getString(R.string.movie_removed_from_DB_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.getDatabaseLiveData().observe(viewLifecycleOwner, {
            adapter.setFilms(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}