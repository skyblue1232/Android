package com.example.flo

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding
import java.util.*

class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 클릭 인터페이스
    interface MyItemClickListener{
        fun onItemClick(album: Album)

        fun onPlayClick(album: Album)
//        fun onRemoveAlbum(position: Int)
    }



    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick(albumList[position])
        }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener{
            mItemClickListener.onPlayClick(albumList[position])
        }
//        holder.binding.itemAlbumPlayImgIv.setOnClickListener{
//            mItemClickListener.onRemoveAlbum(position)
//        }
    }
    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

//    fun removeItem(position: Int){
//        albumList.removeAt(position)
//        notifyDataSetChanged()
//    }


    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)

        }
    }
    private fun Dummy() : ArrayList<Album>{
        val songPlayArr = ArrayList<Song>()
        val songFirstDummy = Song("TimmyTrumpet", "Timmy", 0, 96, false, "timmy_trumpet")
        songPlayArr.add(songFirstDummy)

        val dummy1 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp, songPlayArr)
        val dummy2 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2, songPlayArr)
        val dummy3 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3, songPlayArr)
        val dummy4 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4, songPlayArr)
        val dummy5 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5, songPlayArr)
        val dummy6 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6, songPlayArr)

        val arr = ArrayList<Album>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)
        arr.add(dummy6)

        return arr
    }

}