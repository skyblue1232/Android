package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.MusicItemBinding

class LockerAlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<LockerAlbumRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: MyItemClickListener

    interface MyItemClickListener {
        fun onRemoveAlbum(position: Int)
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, albumList.size)
    }

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LockerAlbumRVAdapter.ViewHolder {
        val binding: MusicItemBinding = MusicItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])

        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    // ViewHolder 클래스
    inner class ViewHolder(val binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemSongImgIv.setImageResource(album.coverImg!!)
            binding.itemSongTitleTv.text = album.title
            binding.itemSongSingerTv.text = album.singer
        }
    }
}