package com.umbrella.appmovies.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.umbrella.appmovies.databinding.FragmentFilmDescriptionBinding
import com.umbrella.appmovies.ui.main.model.pojo.Film

class FilmDescriptionFragment : Fragment() {

    private var _binding: FragmentFilmDescriptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val film = it.getSerializable("film") as Film
            binding.filmDescriptionTitle.text = film.title
            Picasso.get()
                .load(film.posterUrl)
                .into(binding.filmDescriptionPoster)
            binding.filmDescriptionYear.text = film.year.toString()
            binding.filmDescriptionRating.text = film.rating.toString()
            binding.filmDescriptionDescription.text = film.description
        }
    }
}