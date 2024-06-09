package com.example.flo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumsongBinding

class AlbumSongRVAdapter(val context: Context, val result : ArrayList<AlbumSongResult>) : RecyclerView.Adapter<AlbumSongRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumSongRVAdapter.ViewHolder {
        val binding: ItemAlbumsongBinding = ItemAlbumsongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumSongRVAdapter.ViewHolder, position: Int) {
        //holder.bind(result.songs[position])

        holder.title.text = result[position].title
        holder.singer.text = result[position].singer
        if(result[position].songIdx < 10){
            holder.songIdx.text = "0"+result[position].songIdx.toString()
        }else{
            holder.songIdx.text = result[position].songIdx.toString()
        }
        if(result[position].isTitleSong == "T"){
            holder.isTitle.visibility = View.VISIBLE
        }


    }

    override fun getItemCount(): Int = result.size


    inner class ViewHolder(val binding: ItemAlbumsongBinding) : RecyclerView.ViewHolder(binding.root){

        val songIdx : TextView = binding.songListOrderTv
        val isTitle : TextView = binding.songListTitleTv
        val title : TextView = binding.songMusicTitleTv
        val singer : TextView = binding.songSingerNameTv

//        fun bind(song: FloChartSongs){
//            if(song.coverImgUrl == "" || song.coverImgUrl == null) {
//            } else {
//                Glide.with(context).load(song.coverImgUrl).into(binding.itemSongImgIv)
//            }
//
//            binding.itemSongTitleTv.text = song.title
//            binding.itemSongSingerTv.text = song.singer
//        }
    }

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}
