package xyz.katiedotson.restaurantsapp.domain.usecase

import xyz.katiedotson.restaurantsapp.data.repo.RestaurantsRepository
import xyz.katiedotson.restaurantsapp.domain.model.Restaurant
import javax.inject.Inject

class GetInitialRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository,
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
) {

    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }

}
