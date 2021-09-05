package com.umbrella.appmovies.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.FragmentParametersBinding
import com.umbrella.appmovies.view.fragments.FilmsFragment.Companion.IS_INCLUDE_ADULT_KEY

class ParametersFragment : Fragment() {

    private var _binding: FragmentParametersBinding? = null
    private val binding get() = _binding!!
    private var isIncludeAdult = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParametersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIsIncludeAdultVar()
        binding.switchButtonIncludeAdultContent.isChecked = isIncludeAdult
        binding.switchButtonIncludeAdultContent.setOnCheckedChangeListener { _, isChecked ->
            activity?.let {
                it.getPreferences(Context.MODE_PRIVATE).edit().putBoolean(IS_INCLUDE_ADULT_KEY, isChecked).apply()
            }
        }
        binding.FABLocation.setOnClickListener {
            findNavController().navigate(R.id.mapsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initIsIncludeAdultVar() {
        activity?.let {
            isIncludeAdult = it.getPreferences(Context.MODE_PRIVATE).getBoolean(
                IS_INCLUDE_ADULT_KEY,
                false
            )
        }
    }
}