package com.example.flo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: Context) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onPlayClick(album: Album)   // 재생 버튼 클릭시 수행
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰홀더에 던져줍니다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

//    fun addItem(album: Album){
//        albumList.add(album)
//        notifyDataSetChanged()
//    }

//    fun removeItem(position: Int){
//        albumList.removeAt(position)
//        notifyDataSetChanged()
//    }

    // 뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        // holder.bind(albumList[position])

//        if(albumList.albums[position].coverImgUrl == "" || albumList.albums[position].coverImgUrl == null){
//
//        } else {
//            Log.d("image",albumList.albums[position].coverImgUrl )
//            Glide.with(context).load(albumList.albums[position].coverImgUrl).into(holder.coverImg)
//        }
//        holder.title.text = albumList.albums[position].title
//        holder.singer.text = albumList.albums[position].singer

        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(album) }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            mItemClickListener.onPlayClick(album)
        }
        // holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) } //삭제됐을 때
    }

    // 데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = albumList.album.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
            val coverImg : ImageView = binding.itemAlbumCoverImgIv
            val title : TextView = binding.itemAlbumTitleTv
            val singer : TextView = binding.itemAlbumSingerTv

            // fun bind(album: Album){
            // binding.itemAlbumTitleTv.text = album.title
            // binding.itemAlbumSingerTv.text = album.singer
            // binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}