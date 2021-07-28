package com.umbrella.appmovies.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentFilmsBinding
import com.umbrella.appmovies.ui.main.adapter.FilmsAdapter
import com.umbrella.appmovies.ui.main.model.AppState
import com.umbrella.appmovies.ui.main.model.pojo.Film
import com.umbrella.appmovies.ui.main.viewmodel.MainViewModel

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var films: List<Film> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        if (films.isEmpty()) {
            viewModel.getFilmsFromServer()
        } else {
            populateData()
        }
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success -> {
                val films = data.films
                binding.loadingLayout.visibility = View.GONE
                this.films = films
                populateData()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.main, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getFilmsFromServer() }
                    .show()
            }
        }
    }

    private fun populateData() {
        val adapter = FilmsAdapter()
        adapter.upgradeFilms(films)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
        adapter.setOnFilmClickListener(object : FilmsAdapter.OnFilmClickListener {
            override fun clickOnFilm(position: Int) {
                val film = films[position]
                val bundle = Bundle()
                bundle.putSerializable("film", film)
                findNavController().navigate(R.id.filmDescriptionFragment, bundle)
            }
        })
    }
}