package com.example.test2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerViewAdapter(private val itemList: List<ItemClass>) :
        RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainImageView: CircleImageView = itemView.findViewById(R.id.image_view)
        val userTextView: TextView = itemView.findViewById(R.id.user_name)
        val resourceTextView: TextView = itemView.findViewById(R.id.resource_data)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val languageTextView: TextView = itemView.findViewById(R.id.language)
        val starsTextView: TextView = itemView.findViewById(R.id.stars)
        val sharesTextView: TextView = itemView.findViewById(R.id.shares)
        val cardView: RelativeLayout = itemView.findViewById(R.id.total_data)
        val expandableView: RelativeLayout = itemView.findViewById(R.id.rest_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem,
            parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.mainImageView.setImageResource(currentItem.main_image_source)
        holder.userTextView.text = currentItem.user_name
        holder.resourceTextView.text = currentItem.resource_data
        holder.descriptionTextView.text = currentItem.description
        holder.languageTextView.text = currentItem.language
        holder.starsTextView.text = currentItem.stars
        holder.sharesTextView.text = currentItem.shares

        val isExpandable: Boolean = itemList[position].expandable
        holder.expandableView.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.cardView.setOnClickListener {
            val currentItem = itemList[position]
            currentItem.expandable = !currentItem.expandable
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = itemList.size

}