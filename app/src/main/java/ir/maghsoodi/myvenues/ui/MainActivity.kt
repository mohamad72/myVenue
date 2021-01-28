package ir.maghsoodi.myvenues.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activateFragment(venueListFragment)
        subscribeToVenueFlow()
    }

    override fun onResume() {
        super.onResume()
        if (Utils.hasLocationPermission(this))
            updateListWithCurrentLocation()
        else
            getLocationPermission()
    }

    private fun updateListWithCurrentLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            0F,
            object : LocationListener {
                override fun onLocationChanged(p0: Location) {
                    viewModel.getNearVenues(p0.latitude, p0.longitude)
                }

                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

                }

                override fun onProviderEnabled(p0: String?) {
                }

                override fun onProviderDisabled(p0: String?) {
                }

            })

        val localGpsLocation =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (localGpsLocation != null)
            viewModel.getNearVenues(localGpsLocation.latitude, localGpsLocation.longitude)
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

    private fun subscribeToVenueFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.venuesFlow.collect { event ->
                Timber.tag("observer_activity").d("updateVenueList ${event}")
                when (event) {
                    is MainRepository.SearchEvent.Success -> {
                        venueListFragment.updateList(event.venueEntities)
                    }
                    is MainRepository.SearchEvent.Failure -> {
                        Toast.makeText(this@MainActivity,event.errorText,Toast.LENGTH_LONG).show()
                    }
                    is MainRepository.SearchEvent.Loading -> {
                        venueListFragment.startLoading()
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
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            getLocationPermission()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        updateListWithCurrentLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}