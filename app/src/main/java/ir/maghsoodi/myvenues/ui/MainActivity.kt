package ir.maghsoodi.myvenues.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import ir.maghsoodi.myvenues.R
import ir.maghsoodi.myvenues.databinding.ActivityMainBinding
import ir.maghsoodi.myvenues.main.MainViewModel
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.ui.fragments.NoGPSFragment
import ir.maghsoodi.myvenues.ui.fragments.NoInternetFragment
import ir.maghsoodi.myvenues.ui.fragments.VenueListFragment
import ir.maghsoodi.myvenues.utils.Constants.Companion.INTERNET_IS_ONLINE_MESSAGE
import ir.maghsoodi.myvenues.utils.Constants.Companion.LOCATION_LAT_CAFE_BAZAAR
import ir.maghsoodi.myvenues.utils.Constants.Companion.LOCATION_LNG_CAFE_BAZAAR
import ir.maghsoodi.myvenues.utils.Constants.Companion.REQUEST_CODE_LOCATION_PERMISSION
import ir.maghsoodi.myvenues.utils.Utils
import ir.maghsoodi.myvenues.utils.Utils.isGPSEnable
import kotlinx.coroutines.flow.collect
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()

    private val venueListFragment: VenueListFragment = VenueListFragment()
    private val noInternetFragment: NoInternetFragment = NoInternetFragment()
    private val noGPSFragment: NoGPSFragment = NoGPSFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun activateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragments.id, fragment)
            commit()
        }
    }

    lateinit var request: WorkRequest

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(
                DeviceIsOnline, IntentFilter(
                    INTERNET_IS_ONLINE_MESSAGE
                )
            )
    }

    override fun onResume() {
        super.onResume()
        activateFragment(venueListFragment)
        Timber.tag("location changed").d("onResume ${Utils.hasLocationPermission(this)}")
        if (Utils.hasLocationPermission(this))
            checkGPSIsOn()
        else
            getLocationPermission()
    }

    private fun checkGPSIsOn() {
        if (isGPSEnable(this))
            startProcessAfterGrantLocation()
        else
            activateFragment(noGPSFragment)
    }

    private fun startProcessAfterGrantLocation() {
        Timber.tag("internet is back").e("skjjksnfkjshkf")
        updateVenueWithLastLocation()
        subscribeToVenueFlow()
        setListnerForLocationChanger()
        setupChannelForTurningInternetOn()
    }

    private fun subscribeToVenueFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.venuesFlow.collect { event ->
                Timber.tag("internet is back").d("updateVenueList ${event}")
                when (event) {
                    is MainRepository.SearchEvent.Success -> {
                        activateFragment(venueListFragment)
                        venueListFragment.updateList(event.venueEntities)
                    }
                    is MainRepository.SearchEvent.Failure -> {
                        activateFragment(noInternetFragment)
                    }
                    is MainRepository.SearchEvent.Loading -> {
                        activateFragment(venueListFragment)
                        venueListFragment.startLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setListnerForLocationChanger() {
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
                    updateVenueWithThisLocation(p0.latitude, p0.longitude)
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
            updateVenueWithThisLocation(localGpsLocation.latitude, localGpsLocation.longitude, true)
        else {
            Toast.makeText(applicationContext, getString(R.string.gps_is_weak), Toast.LENGTH_LONG)
                .show()
            updateVenueWithLastLocation()
        }
    }

    private fun updateVenueWithThisLocation(lat: Double, lng: Double, isForce: Boolean = false) {
        Timber.tag("location changed").d("new location :$lat, $lng")
        viewModel.getNearVenues(lat, lng, isForce)
    }

    private fun updateVenueWithLastLocation() {
        viewModel.getNearVenues(true)
    }

    private fun setupChannelForTurningInternetOn() {
        if (Utils.hasInternetConnection(this))
            return
        if (!Utils.hasLocationPermission(this))
            return

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        request =
            OneTimeWorkRequest.Builder(MyWork::class.java)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueue(request)
    }

    private fun getLocationPermission() {
        if (Utils.hasLocationPermission(this))
            return

        EasyPermissions.requestPermissions(
            this,
            "لطفا برای ادامه کار اجازه دسترسی به لوکیشن بدهید",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            getLocationPermission()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        checkGPSIsOn()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    var DeviceIsOnline: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            turnInternetOnline()
        }
    }

    private fun turnInternetOnline() {
        Timber.tag("internet").d("internet is back")
        activateFragment(venueListFragment)
        viewModel.getNearVenues(true)
    }


    override fun onPause() {
        super.onPause()
        try {
            WorkManager.getInstance().cancelWorkById(request.id)
        } catch (e: Exception) {
        }

    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(DeviceIsOnline);
    }
}