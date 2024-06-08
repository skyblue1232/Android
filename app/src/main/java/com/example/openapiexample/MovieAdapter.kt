package com.example.openapiexample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R

class MovieAdapter(private val items: List<DailyBoxOfficeList>?) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

//    init {
//        this.items.addAll(items) // 방어적 복사
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        Log.d("MovieAdapter", "Binding item at position $position: ${item!!.movieNm}")
        holder.setItem(item)
    }

    override fun getItemCount(): Int = items?.size!!

//    fun updateData(newItems: List<DailyBoxOfficeList>) {
//        items.clear()!!
//        items.addAll(newItems)
//        notifyDataSetChanged()
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rank: TextView = itemView.findViewById(R.id.rank)
        private val movieNm: TextView = itemView.findViewById(R.id.movieNM)
        private val openDt: TextView = itemView.findViewById(R.id.openDt)

        fun setItem(item: DailyBoxOfficeList) {
            rank.text = item.rank
            movieNm.text = item.movieNm
            openDt.text = item.openDt
        }
    }
}
