package ir.maghsoodi.myvenues.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.maghsoodi.myvenues.databinding.ActivityMainBinding
import ir.maghsoodi.myvenues.main.MainViewModel
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.ui.fragments.VenueListFragment
import ir.maghsoodi.myvenues.utils.Constants.Companion.REQUEST_CODE_LOCATION_PERMISSION
import ir.maghsoodi.myvenues.utils.Utils
import kotlinx.coroutines.flow.collect
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activateFragment(venueListFragment)


        if(Utils.hasLocationPermission(this))
            updateVenueList()
        else
            getLocationPermission()
    }

    private fun getLocationPermission() {
        if (Utils.hasLocationPermission(this))
            return

        EasyPermissions.requestPermissions(
            this,
            "لطفا برای ادامه کار اجازه دسترسی به لوکیشن بدهید",
            REQUEST_CODE_LOCATION_PERMISSION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun updateVenueList() {
        viewModel.getNearVenues(35.7730901, 51.3866738)

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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            getLocationPermission()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        updateVenueList()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}