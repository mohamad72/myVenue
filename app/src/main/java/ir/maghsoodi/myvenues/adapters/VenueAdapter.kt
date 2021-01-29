package ir.maghsoodi.myvenues.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair as UtilPair
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ir.maghsoodi.myvenues.R
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.ui.VenueDetail
import ir.maghsoodi.myvenues.utils.Utils.getImage
import kotlinx.android.synthetic.main.item_venue.view.*


class VenueAdapter(val activity: Activity) :
    RecyclerView.Adapter<VenueAdapter.ArticleViewHolder>() {

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
            layoutDirection = View.LAYOUT_DIRECTION_LTR

            tv_title.text = if (venueEntity.name.split("|").size > 1)
                venueEntity.name.split("|")[1]
            else
                venueEntity.name.split("|")[0]

            tv_description.text = venueEntity.location.getShortAddress()

            tv_distance.text = venueEntity.location.distance.toString()

            if (venueEntity.categories.isNotEmpty())
                iv_category.setImageResource(getImage(venueEntity.categories[0].name))

            setOnClickListener {
                val options: ActivityOptionsCompat? =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        UtilPair.create(iv_category, iv_category.transitionName),
                        UtilPair.create(tv_description, tv_description.transitionName),
                        UtilPair.create(tv_title, tv_title.transitionName)
                    )
                val intent = Intent(activity, VenueDetail::class.java)
                    .putExtra(VenueDetail.venueClicked, Gson().toJson(venueEntity))
                activity.startActivity(intent, options?.toBundle())
            }
        }
    }

    fun setOnItemClickListener(listener: (VenueEntity) -> Unit) {
        onItemClickListener = listener
    }
}