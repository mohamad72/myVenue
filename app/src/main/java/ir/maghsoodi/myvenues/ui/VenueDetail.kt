package ir.maghsoodi.myvenues.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.databinding.ActivityVenueDetailBinding
import ir.maghsoodi.myvenues.main.VenueDetailModel
import ir.maghsoodi.myvenues.utils.Utils

class VenueDetail : AppCompatActivity() {

    companion object {
        const val venueClicked = "venueClicked"
    }

    private lateinit var binding: ActivityVenueDetailBinding
    val viewModel: VenueDetailModel by viewModels()

    lateinit var venueEntity: VenueEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVenueEntity()

        updateViews()

        setOnClickForMapButton()
    }

    private fun setOnClickForMapButton() {
        binding.bMap.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=${venueEntity.location.lat},${venueEntity.location.lng}")
            )
            startActivity(intent)
        }
    }

    private fun updateViews() {
        binding.tvTitle.text = if (venueEntity.name.split("|").size > 1)
            venueEntity.name.split("|")[1]
        else
            venueEntity.name.split("|")[0]

        if (venueEntity.categories.isNotEmpty())
            binding.ivCategory.setImageResource(Utils.getImage(venueEntity.categories[0].name))

        binding.tvAddress.text = viewModel.getFullAddress(venueEntity)
    }

    private fun setVenueEntity() {
        venueEntity = viewModel.getVenueEntity(intent.getStringExtra(venueClicked))
    }
}