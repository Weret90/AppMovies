package com.umbrella.appmovies.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.umbrella.appmovies.databinding.ItemActorBinding
import com.umbrella.appmovies.model.Actor

private const val ACTOR_IMAGE_URL = "https://image.tmdb.org/t/p/original"

class ActorsAdapter : RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>() {

    private var actors: List<Actor> = ArrayList()
    private var onActorClick: (Actor) -> Unit = {}

    fun setOnActorClickListener(onActorCLick: (Actor) -> Unit) {
        this.onActorClick = onActorCLick
    }

    fun setActors(actors: List<Actor>) {
        this.actors = actors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val binding = ItemActorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        val actor = actors[position]
        holder.bind(actor)
    }

    override fun getItemCount(): Int {
        return actors.size
    }

    inner class ActorsViewHolder(private val binding: ItemActorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(actor: Actor) {
            binding.apply {
                actorName.text = actor.name
                Picasso.get().load(ACTOR_IMAGE_URL + actor.profilePath).into(actorImage)
                root.setOnClickListener {
                    onActorClick(actor)
                }
            }
        }
    }
}
