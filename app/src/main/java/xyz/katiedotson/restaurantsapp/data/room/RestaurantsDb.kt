package xyz.katiedotson.restaurantsapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalRestaurant::class],
    version = 2,
    exportSchema = false
)
abstract class RestaurantsDb : RoomDatabase() {
    abstract val dao: RestaurantDao
}
