package com.dicoding.mygithubuserapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubuserapp.databinding.ItemUserBinding
import kotlinx.android.synthetic.main.item_user.view.*


class ListUserAdapter (): RecyclerView.Adapter<ListUserAdapter.RecyclerViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData =ArrayList<User>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<User>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(user: User){
            with(itemView){
                Glide.with(itemView.context)
                        .load(user.avatar)
                        .into(img_avatar)
                binding.txtUsername.text = user.username
                binding.txtId.text = user.id.toString()
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    interface OnItemClickCallback{
        fun onItemClicked(user: User)
    }


}