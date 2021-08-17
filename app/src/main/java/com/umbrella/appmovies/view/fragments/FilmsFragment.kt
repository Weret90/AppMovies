package com.umbrella.appmovies.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentFilmsBinding
import com.umbrella.appmovies.model.AppState
import com.umbrella.appmovies.model.FilmsList
import com.umbrella.appmovies.service.DetailsService
import com.umbrella.appmovies.view.adapters.FilmsAdapter
import com.umbrella.appmovies.view.hide
import com.umbrella.appmovies.view.show
import com.umbrella.appmovies.view.showSnackBar
import com.umbrella.appmovies.viewmodel.MainViewModel

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_RESPONSE_ERROR_EXTRA = "RESPONSE ERROR"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_FILMS_LIST_1 = "FILMS LIST 1"
const val DETAILS_FILMS_LIST_2 = "FILMS LIST 2"
const val DETAILS_FILMS_LIST_3 = "FILMS LIST 3"

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val listAdapters = ArrayList<FilmsAdapter>()

    companion object {
        const val HORROR = "27"
        const val COMEDY = "35"
        const val ACTION = "28"
        const val ARG_FILM = "film"
        private const val SNACK_BAR_ERROR = "Ошибка"
        private const val SNACK_BAR_RELOAD = "Повторить"
        private const val FILMS_CATEGORY_NUMBER = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(inflater, container, false)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (listAdapters.isEmpty()) {
            getAllFilms()
        } else {
            initAdapters()
        }

    }

    private fun getAllFilms() {
        binding.errorScreen.hide()
        binding.loadingLayout.show()
        context?.let { it.startService(Intent(it, DetailsService::class.java)) }
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> {
                    Toast.makeText(context, DETAILS_INTENT_EMPTY_EXTRA, Toast.LENGTH_SHORT).show()
                }
                DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                    binding.loadingLayout.hide()
                    binding.errorScreen.hide()
                    for (i in (1..FILMS_CATEGORY_NUMBER)) {
                        listAdapters.add(FilmsAdapter())
                    }
                    setClickListeners()
                    initAdapters()
                    val filmsList1 = intent.getSerializableExtra(DETAILS_FILMS_LIST_1) as FilmsList
                    val filmsList2 = intent.getSerializableExtra(DETAILS_FILMS_LIST_2) as FilmsList
                    val filmsList3 = intent.getSerializableExtra(DETAILS_FILMS_LIST_3) as FilmsList
                    listAdapters[0].setFilms(filmsList1.films)
                    listAdapters[1].setFilms(filmsList2.films)
                    listAdapters[2].setFilms(filmsList3.films)
                }
                DETAILS_RESPONSE_ERROR_EXTRA -> {
                    binding.loadingLayout.hide()
                    binding.errorScreen.show()
                }
            }
        }
    }

    private fun initMainObserver() {
        viewModel.getNetworkLiveData().observe(viewLifecycleOwner, {
            with(binding) {
                when (it) {
                    is AppState.Loading -> {
                        loadingLayout.show()
                        errorScreen.hide()
                    }
                    is AppState.Success -> {
                        loadingLayout.hide()
                        errorScreen.hide()
                        for (i in listAdapters.indices) {
                            listAdapters[i].setFilms(it.films[i].films)
                        }
                    }
                    is AppState.Error -> {
                        loadingLayout.hide()
                        errorScreen.show()
                        anchorView.showSnackBar(SNACK_BAR_ERROR, SNACK_BAR_RELOAD) {
                            viewModel.makeApiCall(HORROR, ACTION, COMEDY)
                        }
                    }
                }
            }
        })
    }

    private fun initAdapters() {
        val listRecyclerViews = ArrayList<RecyclerView>()

        listRecyclerViews.add(binding.recyclerViewHorrors)
        listRecyclerViews.add(binding.recyclerViewActions)
        listRecyclerViews.add(binding.recyclerViewComedies)

        for (i in listAdapters.indices) {
            listRecyclerViews[i].adapter = listAdapters[i]
        }
    }

    private fun setClickListeners() {
        for (adapter in listAdapters) {
            adapter.setOnFilmClickListener { film ->
                val bundle = Bundle()
                bundle.putSerializable(ARG_FILM, film)
                findNavController().navigate(R.id.filmDetailFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        _binding = null
    }
}