package com.example.flo

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.databinding.MusicItemBinding
import java.nio.file.WatchEvent

class SavedSongRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private val Status = SparseBooleanArray(albumList.size)


    interface SavedSongClickListener{
        fun onRemoveSavedSong(position: Int)
    }

    private lateinit var mItemClickListener: SavedSongClickListener

    fun setSavedSongClickListener(itemClickListener: SavedSongClickListener){
        mItemClickListener = itemClickListener
    }

    fun removeSong(position: Int){
        albumList.removeAt(position)
        Status.delete(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: MusicItemBinding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = albumList.size

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])


        holder.binding.itemSongMoreIv.setOnClickListener{mItemClickListener.onRemoveSavedSong(position)}
//        holder.binding.itemSongMoreIv.isChecked = Status.get(position, false)
//        holder.binding.itemSongMoreIv.setOnClickListener {
//            val checked = !holder.binding.itemSongDownIv.isChecked
//            Status.put(position, checked)
//            notifyItemChanged(position)
//        } 여기에 작성 하는 것 아님.
    }

    inner class ViewHolder(val binding: MusicItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemSongTitleTv.text= album.title
            binding.itemSongSingerTv.text= album.singer
            binding.itemSongImgIv.setImageResource(album.coverImg!!)

//            binding.itemSongPlayIv.isChecked= Status[adapterPosition]
//            binding.itemSongPlayIv.setOnClickListener{
//                if (!binding.itemSongPlayIv.isChecked)
//                    Status.put(adapterPosition, false)
//                else
//                    Status.put(adapterPosition, true)
//                notifyItemChanged(adapterPosition)
//            }
        }
    }
}