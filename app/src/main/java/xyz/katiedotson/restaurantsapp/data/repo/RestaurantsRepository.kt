package xyz.katiedotson.restaurantsapp.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import xyz.katiedotson.restaurantsapp.data.room.LocalRestaurant
import xyz.katiedotson.restaurantsapp.data.room.PartialLocalRestaurant
import xyz.katiedotson.restaurantsapp.data.api.RestaurantsApiService
import xyz.katiedotson.restaurantsapp.data.room.RestaurantDao
import xyz.katiedotson.restaurantsapp.domain.model.Restaurant
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class RestaurantsRepository @Inject constructor(private val service: RestaurantsApiService, private val restaurantsDao: RestaurantDao) {

    suspend fun loadRestaurants() = withContext(Dispatchers.IO) {
        try {
            val remoteRestaurants = service.getRestaurants()
            val favoriteRestaurants = restaurantsDao.getAllFavorited()
            restaurantsDao.addAll(remoteRestaurants.map {
                LocalRestaurant(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    isFavorite = false
                )
            })
            restaurantsDao.updateAll(
                favoriteRestaurants.map {
                    PartialLocalRestaurant(it.id, true)
                }
            )
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException,
                is ConnectException,
                is HttpException -> {
                    if (restaurantsDao.getAll().isEmpty()) throw Exception("Something went wrong. We have no data.")
                }
                else -> throw e
            }
        }
    }

    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialLocalRestaurant(
                    id = id,
                    isFavorite = value
                )
            )
            restaurantsDao.getAll()
        }

    suspend fun getCachedRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.getAll().map {
                Restaurant(
                    it.id, it.title,
                    it.description, it.isFavorite
                )
            }
        }
    }
}
