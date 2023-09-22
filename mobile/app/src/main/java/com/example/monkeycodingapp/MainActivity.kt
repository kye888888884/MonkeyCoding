package com.example.monkeycodingapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class MainActivity : ComponentActivity() {

    // http 통신에 필요한 변수
    private val retrofit = RetrofitInstance.getInstance().create(MyApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 카메라
        val camera = findViewById<Button>(R.id.btn)
        camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResult.launch(intent)
        }
    }

    //결과 가져오기
    @SuppressLint("SimpleDateFormat")
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            @Suppress("DEPRECATION") val bitmap: Bitmap = intent?.extras?.get("data") as Bitmap

            // ImageView에 bitmap 넣기
            val imageView = findViewById<ImageView>(R.id.iv)
            imageView.setImageBitmap(bitmap)

            // bitmap을 file로 변환
            val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
            val file = saveBitmapToFile(bitmap, fileName)

            // file의 용량 확인
            val fileSizeKb = file.length() / 1024
            Log.d("MainActivity", "fileSizeKb: $fileSizeKb")

            // http 통신
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val editText = findViewById<EditText>(R.id.et)

            sendImage(body, editText.text.toString())
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, fileName: String): File {
        val file = File(cacheDir, fileName)
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }

    private fun sendImage(body: MultipartBody.Part, name: String){
        retrofit.sendImage(body, name).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    Toast.makeText(this@MainActivity, "이미지 전송 성공", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "이미지 전송 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("test", t.message.toString())
            }

        })
    }
}

object RetrofitInstance {
    private const val BASE_URL = "https://monkeycoding.kro.kr:8000"

    private val client = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance(): Retrofit {
        return client
    }
}

interface MyApi {
    @Multipart
    @POST("/update")
    fun sendImage(
        @Part file: MultipartBody.Part,
        @Part("name") name: String
    ): Call<String>
}