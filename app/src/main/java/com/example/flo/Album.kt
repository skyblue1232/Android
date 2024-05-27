package com.example.flo

import androidx.room.*

@Entity(tableName = "AlbumTable")
data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
//    val songs: ArrayList<Song>
)

// val songs = ArrayList<Song>()
// 여러 곳 넣고 한 곳 끝나면 다음 곡이 재생되게 해줄 수 있음. (아직은 구현 X).