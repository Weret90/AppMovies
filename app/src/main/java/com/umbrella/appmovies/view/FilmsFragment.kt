package com.umbrella.appmovies.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentFilmsBinding
import com.umbrella.appmovies.view.adapter.FilmsAdapter
import com.umbrella.appmovies.viewmodel.MainViewModel

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private var horrorsAdapter: FilmsAdapter? = null
    private var actionsAdapter: FilmsAdapter? = null
    private var comediesAdapter: FilmsAdapter? = null

    companion object {
        private const val HORROR = "27"
        private const val COMEDY = "35"
        private const val ACTION = "28"
        const val ARG_FILM = "film"
    }

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

        viewModel.getProgressBarLiveData().observe(viewLifecycleOwner, {
            binding.loadingLayout.visibility = View.GONE
        })

        if (horrorsAdapter != null && actionsAdapter != null && comediesAdapter != null) {
            initAdapters()
        } else {
            downloadAllFilms()
            initAdapters()
        }
    }

    private fun downloadAllFilms() {
        horrorsAdapter = getAdapterWithFilmsLiveData(HORROR)
        comediesAdapter = getAdapterWithFilmsLiveData(COMEDY)
        actionsAdapter = getAdapterWithFilmsLiveData(ACTION)
    }

    private fun getAdapterWithFilmsLiveData(genre: String): FilmsAdapter {
        val adapter = FilmsAdapter()
        viewModel.getFilmsLiveData("3", genre).observe(viewLifecycleOwner, {
            adapter.setFilms(it.films)
        })
        adapter.setOnFilmClickListener(object : FilmsAdapter.OnFilmClickListener {
            override fun onFilmClick(position: Int) {
                val film = adapter.getFilms()[position]
                val bundle = Bundle()
                bundle.putSerializable(ARG_FILM, film)
                findNavController().navigate(R.id.filmDetailFragment, bundle)
            }
        })
        return adapter
    }

    private fun initAdapters() {
        binding.recyclerViewHorrors.adapter = horrorsAdapter
        binding.recyclerViewComedies.adapter = comediesAdapter
        binding.recyclerViewActions.adapter = actionsAdapter
    }
}