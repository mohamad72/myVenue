package ir.maghsoodi.myvenues.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.maghsoodi.myvenues.R
import ir.maghsoodi.myvenues.data.models.VenueEntity
import kotlinx.android.synthetic.main.item_venue.view.*


class VenueAdapter : RecyclerView.Adapter<VenueAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<VenueEntity>() {
        override fun areItemsTheSame(oldItem: VenueEntity, newItem: VenueEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VenueEntity, newItem: VenueEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_venue,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((VenueEntity) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val venueEntity = differ.currentList[position]
        holder.itemView.apply {
            tv_title.text = if (venueEntity.name.split("|").size > 1)
                venueEntity.name.split("|")[1]
            else
                venueEntity.name.split("|")[0]

            tv_address.text = venueEntity.location.address
            tv_distance.text = venueEntity.location.distance.toString()

            iv_category.setImageResource(getImage(venueEntity.categories[0].name))
        }
    }

    fun setOnItemClickListener(listener: (VenueEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun getImage(categoryName: String):Int{
        return when(categoryName){
            "Bridge" -> R.drawable.bridge
            "Asian Restaurant","BBQ Joint","Italian Restaurant","Juice Bar","Tabbakhi","Persian Restaurant" -> R.drawable.restaurant
            "Bank" -> R.drawable.bank
            "Bakery" -> R.drawable.bakery
            "Bookstores" -> R.drawable.book_shop
            "Airport" -> R.drawable.airport
            "Sandwich Place","Fast Food Restaurant" -> R.drawable.pizza_shop
            "Voting Booth" -> R.drawable.church
            "Plaza" -> R.drawable.castle
            "Café" -> R.drawable.coffee_shop
            "College Library" -> R.drawable.place_library
            "Auto Dealership","Auto Garage","Car Wash" -> R.drawable.parking
            "Gym / Fitness Center","Gyms or Fitness Centers","Gym","College Gym" -> R.drawable.gym
            "Doctor's Office","Medical Center","Health & Beauty Service" -> R.drawable.hospital
            "Hotel" -> R.drawable.hotel
            "Bus Line","Bus Station" -> R.drawable.bus_station
            "Shopping Mall","Smoke Shop","Gym Pools","Market","Supermarket" -> R.drawable.supermarket
            "Drugstore" -> R.drawable.pharmacy
            "Amphitheater" -> R.drawable.theater
            "Park" -> R.drawable.park
            "Music School","School","Student Center" -> R.drawable.school
            "College Auditorium","College Academic Building","General College & University" -> R.drawable.university
            "Garden" -> R.drawable.zoo
            "Government Building","Business Center","Campaign Office","Office" -> R.drawable.office
            "Military Base" -> R.drawable.police_station
            "Eye Doctor","Dentist's Office" -> R.drawable.clinic
            else -> R.drawable.office
        }

    }
}