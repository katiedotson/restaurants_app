package xyz.katiedotson.restaurantsapp

import kotlinx.coroutines.delay
import xyz.katiedotson.restaurantsapp.data.api.RemoteRestaurant
import xyz.katiedotson.restaurantsapp.data.api.RestaurantsApiService
import xyz.katiedotson.restaurantsapp.testutil.DummyContent

class DummyApiService : RestaurantsApiService {

    override suspend fun getRestaurants() : List<RemoteRestaurant> {
        return DummyContent.getRemoteRestaurants()
    }

    override suspend fun getRestaurant(id: Int) : Map<String, RemoteRestaurant> {
        TODO("Not yet implemented")
    }

}
