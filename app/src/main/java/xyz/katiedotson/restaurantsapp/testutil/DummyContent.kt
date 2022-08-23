package xyz.katiedotson.restaurantsapp.testutil

import xyz.katiedotson.restaurantsapp.data.api.RemoteRestaurant
import xyz.katiedotson.restaurantsapp.domain.model.Restaurant

object DummyContent {
    fun getDomainRestaurants() = arrayListOf(
        Restaurant(id = 0, title = "title0", description = "description0", isFavorite = false),
        Restaurant(id = 1, title = "title1", description = "description1", isFavorite = false),
        Restaurant(id = 2, title = "title2", description = "description2", isFavorite = false),
        Restaurant(id = 3, title = "title3", description = "description3", isFavorite = false)
    )

    fun getRemoteRestaurants() = getDomainRestaurants()
        .map {
            RemoteRestaurant(
                it.id,
                it.title,
                it.description
            )
        }

}
