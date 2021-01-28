package ir.maghsoodi.myvenues.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.maghsoodi.myvenues.R
import ir.maghsoodi.myvenues.databinding.ActivityMainBinding
import ir.maghsoodi.myvenues.databinding.ActivityVenueDeatilBinding

class VenueDetail : AppCompatActivity() {

    private lateinit var binding: ActivityVenueDeatilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueDeatilBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}