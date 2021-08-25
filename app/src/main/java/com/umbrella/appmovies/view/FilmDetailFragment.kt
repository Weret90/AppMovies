package com.umbrella.appmovies.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentFilmDetailBinding
import com.umbrella.appmovies.model.Film
import com.umbrella.appmovies.viewmodel.MainViewModel

class FilmDetailFragment : Fragment() {

    private var _binding: FragmentFilmDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    companion object {
        private const val POSTER_URL = "https://image.tmdb.org/t/p/original"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val film = it.getSerializable(FilmsFragment.ARG_FILM) as Film
            with(binding) {
                filmDescriptionTitle.text = film.title
                val fullPosterUrl = POSTER_URL + film.posterPath
                Picasso.get()
                    .load(fullPosterUrl)
                    .into(binding.filmDescriptionPoster)
                filmDescriptionYear.text = film.releaseDate
                filmDescriptionRating.text = film.voteAverage.toString()
                filmDescriptionDescription.text = film.overview

                addFilmToSelectedFilms.setOnClickListener {
                    viewModel.insertFilmIntoDB(film)
                    Toast.makeText(
                        context,
                        root.resources.getString(R.string.movie_added_into_DB_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}