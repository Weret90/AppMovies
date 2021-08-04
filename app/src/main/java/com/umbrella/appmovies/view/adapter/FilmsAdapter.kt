package com.umbrella.appmovies.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.umbrella.appmovies.databinding.ItemFilmBinding
import com.umbrella.appmovies.model.Film

class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.MyViewHolder>() {

    private var films: List<Film> = ArrayList()
    private var onFilmClickListener: (Film) -> Unit = {}

    companion object {
        private const val POSTER_URL = "https://image.tmdb.org/t/p/original"
    }

    fun setOnFilmClickListener(onFilmClickListener: (Film) -> Unit) {
        this.onFilmClickListener = onFilmClickListener
    }

    fun setFilms(films: List<Film>) {
        this.films = films
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val film = films[position]
        holder.bind(film)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    inner class MyViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            binding.apply {
                filmTitle.text = film.title
                val fullPosterUrl = POSTER_URL + film.posterPath
                Picasso.get()
                    .load(fullPosterUrl)
                    .into(binding.filmPoster)
                filmYear.text = film.releaseDate
                filmRating.text = film.voteAverage.toString()
                itemLinearLayout.setOnClickListener {
                    onFilmClickListener(film)
                }
            }
        }
    }
}