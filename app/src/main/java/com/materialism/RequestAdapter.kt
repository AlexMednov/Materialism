package com.materialism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Request(
    val name: String,
    val description: String,
    val category: String,
    val location: String,
    val date: String,
    val recipient: String? = null,
    val status: String? = null
)

class RequestAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var requestList: List<Request> = listOf()
    private var viewType: ViewType = ViewType.INCOMING

    enum class ViewType {
        INCOMING,
        YOUR
    }

    fun updateData(requestList: List<Request>, viewType: ViewType) {
        this.requestList = requestList
        this.viewType = viewType
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ViewType.INCOMING.ordinal) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_incoming_request, parent, false)
            IncomingRequestViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_your_request, parent, false)
            YourRequestViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val request = requestList[position]
        if (holder is IncomingRequestViewHolder) {
            holder.bind(request)
        } else if (holder is YourRequestViewHolder) {
            holder.bind(request)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    class IncomingRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.item_name)
        private val itemDescription: TextView = itemView.findViewById(R.id.item_description)
        private val itemCategory: TextView = itemView.findViewById(R.id.item_category)
        private val itemLocation: TextView = itemView.findViewById(R.id.item_location)
        private val itemDate: TextView = itemView.findViewById(R.id.item_date)
        private val acceptIcon: ImageView = itemView.findViewById(R.id.accept_icon)
        private val rejectIcon: ImageView = itemView.findViewById(R.id.reject_icon)

        fun bind(request: Request) {
            itemName.text = request.name
            itemDescription.text = request.description
            itemCategory.text = request.category
            itemLocation.text = request.location
            itemDate.text = request.date
        }
    }

    class YourRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.item_name)
        private val itemDescription: TextView = itemView.findViewById(R.id.item_description)
        private val itemCategory: TextView = itemView.findViewById(R.id.item_category)
        private val itemLocation: TextView = itemView.findViewById(R.id.item_location)
        private val itemDate: TextView = itemView.findViewById(R.id.item_date)
        private val recipient: TextView = itemView.findViewById(R.id.request_sent_to)
        private val status: TextView = itemView.findViewById(R.id.status)

        fun bind(request: Request) {
            itemName.text = request.name
            itemDescription.text = request.description
            itemCategory.text = request.category
            itemLocation.text = request.location
            itemDate.text = request.date
            recipient.text = "Request sent to: ${request.recipient}"
            status.text = "Status: ${request.status}"
        }
    }
}
