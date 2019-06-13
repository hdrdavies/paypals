package com.example.paypals.ui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.paypals.R
import com.example.paypals.data.Contact
import com.example.paypals.store
import com.example.paypals.store.DeselectContact
import com.example.paypals.store.SelectContact
import com.squareup.picasso.Picasso

class ContactsListAdapter(val contacts: List<Contact>) : RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        tracker?.let {
            holder.bind(contact, it.isSelected(position.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = contacts.size
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var name: TextView = view.findViewById(R.id.name)
        private var number: TextView = view.findViewById(R.id.number)
        private var image: ImageView = view.findViewById(R.id.image)

        fun bind(contact: Contact, isActivated: Boolean = false) {
            name.text = contact.name
            number.text = contact.number

            Picasso.get()
                .load(contact.avatarUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .transform(CircleTransform())
                .into(image)

            itemView.isActivated = isActivated

            if (isActivated) {
                store.dispatch(SelectContact(contact))
            } else {
                store.dispatch(DeselectContact(contact))
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }
}

class ContactsItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as ContactsListAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}