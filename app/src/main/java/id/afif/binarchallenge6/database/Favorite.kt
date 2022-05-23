package id.afif.binarchallenge6.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") val releaseDate: String,
    @ColumnInfo(name = "poster") val posterPath: String,
    @ColumnInfo(name = "voteAverage") val voteAverage: Double,
)