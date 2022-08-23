package xyz.katiedotson.restaurantsapp

import kotlinx.coroutines.delay
import xyz.katiedotson.restaurantsapp.data.room.LocalRestaurant
import xyz.katiedotson.restaurantsapp.data.room.PartialLocalRestaurant
import xyz.katiedotson.restaurantsapp.data.room.RestaurantDao

class DummyDao : RestaurantDao {

    private var restaurants = HashMap<Int, LocalRestaurant>()

    override suspend fun getAll(): List<LocalRestaurant> {
        return restaurants.values.toList()
    }

    override suspend fun addAll(restaurants: List<LocalRestaurant>) {
        restaurants.forEach {
            this.restaurants[it.id] = it
        }
    }

    override suspend fun update(partialRestaurant: PartialLocalRestaurant) {
        updateRestaurant(partialRestaurant)
    }

    override suspend fun updateAll(partialRestaurants: List<PartialLocalRestaurant>) {
        partialRestaurants.forEach {
            update(it)
        }
    }

    override suspend fun getAllFavorited(): List<LocalRestaurant> {
        return restaurants.values.toList().filter { it.isFavorite }
    }

    private fun updateRestaurant(partialRestaurant: PartialLocalRestaurant) {
        val restaurant = this.restaurants[partialRestaurant.id]
        if (restaurant != null)
            this.restaurants[partialRestaurant.id] =
                restaurant.copy(isFavorite = partialRestaurant.isFavorite)
    }

}


