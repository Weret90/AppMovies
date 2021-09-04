package com.umbrella.appmovies.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentActorsBinding
import com.umbrella.appmovies.model.AllActorsDTO
import com.umbrella.appmovies.view.adapters.ActorsAdapter
import com.umbrella.appmovies.view.fragments.FilmDetailFragment.Companion.ARG_ACTOR
import com.umbrella.appmovies.viewmodel.ActorsViewModel

private const val ACTORS_NUMBER_IN_LIST = 12

class ActorsFragment : Fragment() {

    private var _binding: FragmentActorsBinding? = null
    private val binding get() = _binding!!
    private val actorsAdapter = ActorsAdapter()
    private val actorsViewModel: ActorsViewModel by lazy {
        ViewModelProvider(this).get(ActorsViewModel::class.java)
    }

    companion object {
        const val ARG_PLACE_OF_BIRTH = "placeOfBirth"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val allActors = it.getSerializable(ARG_ACTOR) as AllActorsDTO
            actorsAdapter.setActors(allActors.actors.take(ACTORS_NUMBER_IN_LIST))
            binding.actorsRecyclerView.adapter = actorsAdapter
            actorsAdapter.setOnActorClickListener { actor ->
                actorsViewModel.actorLiveData.observe(viewLifecycleOwner) { actorFullInfo ->
                    val bundle = Bundle()
                    bundle.putString(ARG_PLACE_OF_BIRTH, actorFullInfo.placeOfBirth)
                    findNavController().navigate(R.id.mapsFragment, bundle)
                }
                actorsViewModel.makeApiCalAndGetActorInfo(actor.id)
            }
        }
    }
}