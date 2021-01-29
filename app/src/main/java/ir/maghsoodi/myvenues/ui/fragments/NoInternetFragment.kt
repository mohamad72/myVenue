package ir.maghsoodi.myvenues.ui.fragments

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import ir.maghsoodi.myvenues.R
import kotlinx.android.synthetic.main.fragment_no_internet.*


class NoInternetFragment : Fragment(R.layout.fragment_no_internet) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_cellular.setOnClickListener {
            startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
        }

        b_wifi.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }


}