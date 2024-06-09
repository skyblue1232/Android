import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.AuthResponse
import com.example.flo.LoginActivity
import com.example.flo.MainActivity
import com.example.flo.RetrofitInstance
import com.example.flo.databinding.ActivitySplashBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    private lateinit var spf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences 초기화
        spf = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        editor = spf.edit()

        val handler = Handler(Looper.getMainLooper())

        // 토큰
        val accessToken = getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            // 액세스 토큰이 없으면 로그인 화면으로 이동
            navigateToLogin()
        } else {
            // 액세스 토큰이 있으면 서버에 유효성 확인 요청
            validateAccessToken(accessToken)
        }

        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 1000)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateAccessToken(token: String) {
        val call = RetrofitInstance.authApi.autoLogin("Bearer $token")
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    // 유효하면 메인 화면으로 이동
                    navigateToMain()
                } else {
                    // 유효하지 않으면 refreshToken 확인
                    val refreshToken = getRefreshToken()
                    if (refreshToken.isNullOrEmpty()) {
                        navigateToLogin()
                    } else {
                        refreshAccessToken(refreshToken)
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                // 네트워크 오류 또는 기타 실패 처리
                navigateToLogin()
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun refreshAccessToken(refreshToken: String) {
        // 서버에 refreshToken을 보내 새 accessToken 발급 요청 (예시)
        val newAccessToken = getNewAccessTokenFromServer(refreshToken)

        if (newAccessToken != null) {
            saveTokens(newAccessToken, refreshToken)
            navigateToMain()
        } else {
            navigateToLogin()
        }
    }

    private fun getNewAccessTokenFromServer(refreshToken: String): String {
        // 서버에 refreshToken을 보내 새 accessToken 발급 요청
        // 실제로는 네트워크 요청을 통해 새 토큰 발급
        return "newAccessToken" // 예시로 새 토큰 반환
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }

    private fun getAccessToken(): String? {
        return spf.getString("access_token", null)
    }

    private fun getRefreshToken(): String? {
        return spf.getString("refresh_token", null)
    }
}