package com.dicoding.consumerapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class FavoriteAdapter() : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var onItemClickCallback: FavoriteAdapter.OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    var mData =ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.mData.clear()
            }
            this.mData.addAll(listFavorite)

            notifyDataSetChanged()
        }

    fun setAddToFav(item: User){
        this.mData.add(item)
        notifyItemInserted(this.mData.size - 1)
    }

    fun setRemoveFav(index: Int){
        this.mData.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, this.mData.size)
    }


    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: User){
            with(itemView){
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .into(img_avatar)
                txt_username.text = favorite.username
                txt_id.text = favorite.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(mData[position]) }

    }



    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}