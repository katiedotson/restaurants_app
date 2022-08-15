package xyz.katiedotson.restaurantsapp.features.restaurantslist

import xyz.katiedotson.restaurantsapp.domain.model.Restaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean,
    val error: String? = null
)
