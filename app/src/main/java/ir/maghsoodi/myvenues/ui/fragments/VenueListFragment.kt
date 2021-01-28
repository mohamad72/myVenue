package ir.maghsoodi.myvenues.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.maghsoodi.myvenues.R
import ir.maghsoodi.myvenues.adapters.VenueAdapter
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.main.MainViewModel
import ir.maghsoodi.myvenues.ui.MainActivity
import ir.maghsoodi.myvenues.utils.Constants.Companion.NUMBER_OF_ITEMS_IN_EACH_PAGE
import kotlinx.android.synthetic.main.fragment_venue_list.*


class VenueListFragment : Fragment(R.layout.fragment_venue_list) {

    lateinit var venueAdapter: VenueAdapter
    lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        startLoading()
    }

    fun updateList(venueEntities: List<VenueEntity>) {
        stopLoading()
        venueAdapter.differ.submitList(venueEntities)
    }

    private fun stopLoading() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    fun startLoading() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= NUMBER_OF_ITEMS_IN_EACH_PAGE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getNearVenuesMore()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        venueAdapter = VenueAdapter(requireActivity())
        rv_venues.apply {
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            addOnScrollListener(this@VenueListFragment.scrollListener)
        }
    }
}