package ir.maghsoodi.myvenues.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import ir.maghsoodi.myvenues.R
import ir.maghsoodi.myvenues.adapters.VenueAdapter
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.ui.VenueDetail
import kotlinx.android.synthetic.main.fragment_venue_list.*


class VenueListFragment : Fragment(R.layout.fragment_venue_list) {

    lateinit var venueAdapter: VenueAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        showProgressBar()
    }

    fun updateList(venueEntities: List<VenueEntity>) {
        hideProgressBar()
        venueAdapter.differ.submitList(venueEntities)
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        venueAdapter = VenueAdapter(requireActivity())
        rv_venues.apply {
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        venueAdapter.setOnItemClickListener {

        }
    }
}