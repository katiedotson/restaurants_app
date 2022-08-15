package xyz.katiedotson.restaurantsapp.domain.usecase

import xyz.katiedotson.restaurantsapp.data.repo.RestaurantsRepository
import xyz.katiedotson.restaurantsapp.domain.model.Restaurant
import javax.inject.Inject

class ToggleRestaurantUseCase @Inject constructor(
    private val repository: RestaurantsRepository,
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
) {

    suspend operator fun invoke(
        id: Int,
        oldValue: Boolean
    ): List<Restaurant> {
        val isNewFav = !oldValue
        repository.toggleFavoriteRestaurant(id, isNewFav)
        return getSortedRestaurantsUseCase()
    }

}
