package com.umbrella.appmovies.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.umbrella.appmovies.R
import com.umbrella.appmovies.ui.main.model.pojo.Film

class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.MyViewHolder>() {

    private var films: List<Film> = ArrayList()

    interface OnFilmClickListener {
        fun clickOnFilm(position: Int)
    }

    private var onFilmClickListener: OnFilmClickListener? = null

    fun setOnFilmClickListener(onFilmClickListener: OnFilmClickListener?) {
        this.onFilmClickListener = onFilmClickListener
    }

    fun upgradeFilms(films: List<Film>) {
        this.films = films
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val film = films[position]
        holder.filmTitle.text = film.title
        Picasso.get()
            .load(film.posterUrl)
            .into(holder.filmPoster)
        holder.filmYear.text = film.year.toString()
        holder.filmRating.text = film.rating.toString()
    }

    override fun getItemCount(): Int {
        return films.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filmTitle: TextView = itemView.findViewById(R.id.filmTitle)
        val filmYear: TextView = itemView.findViewById(R.id.filmYear)
        val filmPoster: ImageView = itemView.findViewById(R.id.filmPoster)
        val filmRating: TextView = itemView.findViewById(R.id.filmRating)

        init {
            itemView.setOnClickListener {
                if (onFilmClickListener != null) {
                    onFilmClickListener!!.clickOnFilm(adapterPosition)
                }
            }
        }
    }
}