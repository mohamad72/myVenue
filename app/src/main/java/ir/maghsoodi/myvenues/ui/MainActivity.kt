package ir.maghsoodi.myvenues.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.maghsoodi.myvenues.adapters.VenueAdapter
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.databinding.ActivityMainBinding
import ir.maghsoodi.myvenues.main.MainViewModel
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.ui.fragments.VenueListFragment
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activateFragment(venueListFragment)
        updateVenueList()
    }

    private fun updateVenueList() {
        viewModel.getNearVenues()

        lifecycleScope.launchWhenStarted {
            viewModel.venusFlow.collect { event ->
                Timber.tag("loadData").d("updateVenueList ${event}")
                when (event) {
                    is MainRepository.SearchEvent.Success -> {
                        venueListFragment.updateList(event.venueEntities)
                    }
                    is MainRepository.SearchEvent.Failure -> {
                        Timber.tag("loadData").e("failed updateVenueList ${event.errorText}")
                    }
                    is MainRepository.SearchEvent.Loading -> {
                        venueListFragment.showProgressBar()
                    }
                    else -> Unit
                }
            }
        }
    }

    private val venueListFragment: VenueListFragment = VenueListFragment()

    private fun activateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragments.id, fragment)
            commit()
        }
    }


}