package com.example.flo

import android.graphics.Color
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

        holder.binding.mainSwitch.isChecked = holder.binding.mainSwitch.isChecked
    }

    inner class ViewHolder(val binding: MusicItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemSongTitleTv.text= album.title
            binding.itemSongSingerTv.text= album.singer
            binding.itemSongImgIv.setImageResource(album.coverImg!!)

            if (!binding.mainSwitch.isChecked) {
                binding.root.setBackgroundColor(Color.WHITE)
            }
            else {
                binding.root.setBackgroundColor(Color.DKGRAY)
            }

            binding.mainSwitch.isChecked= Status[adapterPosition]
            binding.mainSwitch.setOnClickListener{
                if (!binding.mainSwitch.isChecked)
                    Status.put(adapterPosition, false)
                else
                    Status.put(adapterPosition, true)
                notifyItemChanged(adapterPosition)
            }
        }
    }
}