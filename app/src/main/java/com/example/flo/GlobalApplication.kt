package com.example.flo
package com.example.kakao.utils

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "d2b6dadb737b9c8f553e4483464469b7")
    }
}