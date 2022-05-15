package id.afif.binarchallenge6.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TMDBClient {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    /**
     * Bikin interceptor
     */
    private val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    //create object client untuk retorfit
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    //Membuat instance

    val instance : TMDBService by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(TMDBService::class.java)
    }
}