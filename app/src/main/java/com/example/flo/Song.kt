package com.example.flo

import androidx.room.*

@Entity(tableName = "SongTable")
data class Song(
    @ColumnInfo("musicTitle")
    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    val music: String = "",
    var isLike: Boolean = false,
    var coverImg: Int? = null,
//    @Ignore val dummyData: Int = 0

    val albumIdx : Int = 0
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

