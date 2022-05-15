package id.afif.binarchallenge6.API

import id.afif.binarchallenge6.Model.MovieDetail
import id.afif.binarchallenge6.Model.Movies
import id.afif.binarchallenge6.Model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {
    @GET("movie/popular")
    suspend fun getAllMovie(@Query("api_key") key : String) : Movies

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId : Int,@Query("api_key") key : String) : MovieDetail
}