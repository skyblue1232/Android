package com.example.flo

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null

)

// 여러 곳 넣고 한 곳 끝나면 다음 곡이 재생되게 해줄 수 있음. (아직은 구현 X).