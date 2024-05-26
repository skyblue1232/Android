package com.example.flo

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.MusicItemBinding

class SavedSongRVAdapter(): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private val Status = SparseBooleanArray()
    private val songs = ArrayList<Song>()
    interface SavedSongClickListener{
        fun onRemoveSavedSong(songId: Int)

        fun onSaveSavedSong(songId: Int)
    }

    private lateinit var mItemClickListener: SavedSongClickListener

    fun setSavedSongClickListener(itemClickListener: SavedSongClickListener){
        mItemClickListener = itemClickListener
    }


    @SuppressLint("NotifyDataSetChanged")
    fun removeSong(position: Int){
        songs.removeAt(position)
        Status.delete(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addSong(position: Int){
        songs.add(songs[position])
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: MusicItemBinding = MusicItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])

        holder.binding.itemSongMoreIv.setOnClickListener{
            mItemClickListener.onRemoveSavedSong(songs[position].id)}
            //좋아요 취소
            removeSong(position) // 현재 화면에서 아이템 제거

        holder.binding.mainSwitch.isChecked = holder.binding.mainSwitch.isChecked

        holder.binding.itemSongMoreIv.setOnClickListener{
            mItemClickListener.onSaveSavedSong(songs[position].id)}

            addSong(position)

    }

    inner class ViewHolder(val binding: MusicItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Song){
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
    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

}