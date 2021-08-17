package com.umbrella.appmovies.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.umbrella.appmovies.R
import com.umbrella.appmovies.databinding.ActivityMainBinding
import com.umbrella.appmovies.service.MainBroadcastReceiver

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val receiver = MainBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNavMovies -> findNavController(R.id.fragmentContainerView).navigate(R.id.mainFragment)
                R.id.bottomNavSelectedMovies -> findNavController(R.id.fragmentContainerView).navigate(R.id.selectedFilmsFragment)
                R.id.bottomNavInfo -> findNavController(R.id.fragmentContainerView).navigate(R.id.infoFragment)
            }
            true
        }

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}