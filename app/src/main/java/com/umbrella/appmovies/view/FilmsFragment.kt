package com.umbrella.appmovies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentFilmsBinding
import com.umbrella.appmovies.model.AppState
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
        private const val SNACK_BAR_ERROR = "Ошибка"
        private const val SNACK_BAR_RELOAD = "Повторить"
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

        if (horrorsAdapter != null && actionsAdapter != null && comediesAdapter != null) {
            initAdapters()
        } else {
            initMainObserver()
            downloadAllFilms()
        }
    }

    private fun downloadAllFilms() {
        horrorsAdapter = getAdapterWithFilmsLiveData(HORROR)
        comediesAdapter = getAdapterWithFilmsLiveData(COMEDY)
        actionsAdapter = getAdapterWithFilmsLiveData(ACTION)
        initAdapters()
    }

    private fun getAdapterWithFilmsLiveData(genre: String): FilmsAdapter {
        val adapter = FilmsAdapter()
        viewModel.getFilmsLiveData("1", genre).observe(viewLifecycleOwner, {
            adapter.setFilms(it.films)
        })
        adapter.setOnFilmClickListener { film ->
            val bundle = Bundle()
            bundle.putSerializable(ARG_FILM, film)
            findNavController().navigate(R.id.filmDetailFragment, bundle)
        }
        return adapter
    }

    private fun initMainObserver() {
        viewModel.getMainLiveData().observe(viewLifecycleOwner, {
            when (it) {
                is AppState.Loading -> binding.loadingLayout.show()
                is AppState.Success -> {
                    binding.loadingLayout.hide()
                    binding.errorScreen.hide()
                }
                is AppState.Error -> {
                    binding.loadingLayout.hide()
                    binding.errorScreen.show()
                    binding.root.showSnackBar(SNACK_BAR_ERROR, SNACK_BAR_RELOAD) {
                        downloadAllFilms()
                    }
                }
            }
        })
    }

    private fun initAdapters() {
        binding.recyclerViewHorrors.adapter = horrorsAdapter
        binding.recyclerViewComedies.adapter = comediesAdapter
        binding.recyclerViewActions.adapter = actionsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}