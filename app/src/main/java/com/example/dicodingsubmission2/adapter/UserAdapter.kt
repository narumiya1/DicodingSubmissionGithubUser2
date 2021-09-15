package com.example.dicodingsubmission2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dicodingsubmission2.R
import com.example.dicodingsubmission2.activities.UserDetailActivity
import com.example.dicodingsubmission2.databinding.ItemListUserBinding
import com.example.dicodingsubmission2.models.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val mData = ArrayList<User>()
    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListUserBinding.bind(itemView)
        fun bind(user: User) {
            binding.tvItemUsername.text = user.login
            binding.tvItemLink.text = user.html_url
            Glide.with(itemView.context)
                .load(user.avatar_url)
                .apply(
                    RequestOptions()
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .override(55, 55)
                )
                .into(binding.imgItemFotoUser)

            itemView.setOnClickListener {
                val setObjectIntent = Intent(it.context, UserDetailActivity::class.java)
                setObjectIntent.putExtra(UserDetailActivity.DETAIL_USER, user)
                it.context.startActivity(setObjectIntent)
            }
        }
    }
}