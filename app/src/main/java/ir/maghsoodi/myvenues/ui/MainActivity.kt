package ir.maghsoodi.myvenues.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.maghsoodi.myvenues.adapters.VenueAdapter
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.databinding.ActivityMainBinding
import ir.maghsoodi.myvenues.main.MainViewModel
import ir.maghsoodi.myvenues.main.repository.MainRepository
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateVenueList()
    }

    private fun updateVenueList() {
        viewModel.getNearVenues()

        lifecycleScope.launchWhenStarted {
            viewModel.venusFlow.collect { event ->
                Timber.tag("loadData").d("updateVenueList ${event}")
                when (event) {
                    is MainRepository.SearchEvent.Success -> {
                        setupRecyclerView(event.venueEntities)
                    }
                    is MainRepository.SearchEvent.Failure -> {
                        Timber.tag("loadData").d("failed updateVenueList ${event.errorText}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRecyclerView(input: List<VenueEntity>) {
        Timber.tag("loadData").d("setupRecyclerView "+input.size)
        val venueAdapter = VenueAdapter()
        binding.rvVenues.apply {
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            venueAdapter.differ.submitList(input.toList())
        }
    }
}