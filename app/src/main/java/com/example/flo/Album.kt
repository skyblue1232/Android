package com.example.flo

import androidx.room.*

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
//    val songs: ArrayList<Song>
) {
    val songs = ArrayList<Song>()
}

// 여러 곳 넣고 한 곳 끝나면 다음 곡이 재생되게 해줄 수 있음. (아직은 구현 X).