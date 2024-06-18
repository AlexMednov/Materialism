package com.materialism.utils

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.materialism.databinding.ItemLayoutBinding
import com.materialism.databinding.ItemLayoutMainPageBinding
import com.materialism.sampledata.Item

class ItemAdapter(
    private var items: List<Item>,
    private val showEditButton: Boolean,
    private val imageRenderer: ImageRenderer
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var onClickListener: OnClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (showEditButton) {
      val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      EditViewHolder(binding)
    } else {
      val binding =
          ItemLayoutMainPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      NoEditViewHolder(binding)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    val imageUri = item.imageUri
    var image: Bitmap? = null

    if (holder is EditViewHolder) {

      try {
        image = imageRenderer.getThumbnail(item.imageUri.toUri(), 240)
        holder.binding.itemImage.setImageBitmap(image)
      } catch (e: Exception) {
        Log.e("ImageRenderer", e.toString())
      }

      holder.binding.itemName.text = item.name
      holder.binding.itemDescription.text = item.description
      holder.binding.itemCategory.text = item.category
      holder.binding.itemLocation.text = item.location
      holder.binding.itemDate.text = item.date
      holder.itemView.setOnClickListener { onClickListener?.onClick(position, item) }
    } else if (holder is NoEditViewHolder) {

      try {
        image = imageRenderer.getThumbnail(item.imageUri.toUri(), 240)
        holder.binding.itemImage.setImageBitmap(image)
      } catch (e: Exception) {
        Log.e("ImageRenderer", e.toString())
      }

      holder.binding.itemName.text = item.name
      holder.binding.itemDescription.text = item.description
      holder.binding.itemCategory.text = item.category
      holder.binding.itemLocation.text = item.location
      holder.binding.itemDate.text = item.date
      holder.itemView.setOnClickListener { onClickListener?.onClick(position, item) }
    }
  }

  override fun getItemCount() = items.size

  // Set the click listener for the adapter
  fun setOnClickListener(listener: OnClickListener?) {
    this.onClickListener = listener
  }

  // Interface for the click listener
  interface OnClickListener {
    fun onClick(position: Int, itemModel: Item)
  }

  class EditViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

  class NoEditViewHolder(val binding: ItemLayoutMainPageBinding) :
      RecyclerView.ViewHolder(binding.root)

  // Methods for sorting
  fun sortByCategory() {
    items = items.sortedBy { it.category }
    notifyDataSetChanged()
  }

  fun sortByNameAsc() {
    items = items.sortedBy { it.name }
    notifyDataSetChanged()
  }

  fun sortByNameDesc() {
    items = items.sortedByDescending { it.name }
    notifyDataSetChanged()
  }

  fun sortByLocationAsc() {
    items = items.sortedBy { it.location }
    notifyDataSetChanged()
  }

  fun sortByLocationDesc() {
    items = items.sortedByDescending { it.location }
    notifyDataSetChanged()
  }

  fun sortByDateAsc() {
    items = items.sortedBy { it.date }
    notifyDataSetChanged()
  }

  fun sortByDateDesc() {
    items = items.sortedByDescending { it.date }
    notifyDataSetChanged()
  }
}
