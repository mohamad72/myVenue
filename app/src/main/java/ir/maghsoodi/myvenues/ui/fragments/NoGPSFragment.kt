package ir.maghsoodi.myvenues.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import ir.maghsoodi.myvenues.R
import kotlinx.android.synthetic.main.fragment_no_gps.*


class NoGPSFragment : Fragment(R.layout.fragment_no_gps) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_gps.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }


}