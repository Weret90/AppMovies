package com.umbrella.appmovies.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentFilmsBinding
import com.umbrella.appmovies.model.AppState
import com.umbrella.appmovies.view.adapters.FilmsAdapter
import com.umbrella.appmovies.view.hide
import com.umbrella.appmovies.view.show
import com.umbrella.appmovies.view.showSnackBar
import com.umbrella.appmovies.viewmodel.MainViewModel

private const val HORROR = "27"
private const val COMEDY = "35"
private const val ACTION = "28"
private const val SNACK_BAR_ERROR = "Ошибка"
private const val SNACK_BAR_RELOAD = "Повторить"
private const val FILMS_CATEGORY_NUMBER = 3

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val listAdapters = ArrayList<FilmsAdapter>()
    private var isIncludeAdult = false

    companion object {
        const val ARG_FILM = "film"
        const val IS_INCLUDE_ADULT_KEY = "isIncludeAdultKey"
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

        initIsIncludeAdultVar()

        if (listAdapters.isEmpty()) {
            for (i in (1..FILMS_CATEGORY_NUMBER)) {
                listAdapters.add(FilmsAdapter())
            }
            setClickListeners()
            initAdapters()
            initMainObserver()
            Log.i("proverka", isIncludeAdult.toString())
            viewModel.makeApiCalls(HORROR, ACTION, COMEDY, isIncludeAdult)

        } else {
            initAdapters()
        }

    }

    private fun initIsIncludeAdultVar() {
        activity?.let {
            isIncludeAdult = it.getPreferences(Context.MODE_PRIVATE).getBoolean(
                IS_INCLUDE_ADULT_KEY,
                false
            )
        }
    }

    private fun initMainObserver() {
        viewModel.getDownloadStatusLiveData().observe(viewLifecycleOwner, {
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
                            viewModel.makeApiCalls(HORROR, ACTION, COMEDY, isIncludeAdult)
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
        _binding = null
    }
}