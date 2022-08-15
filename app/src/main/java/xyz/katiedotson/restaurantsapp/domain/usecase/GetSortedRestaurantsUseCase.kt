package xyz.katiedotson.restaurantsapp.domain.usecase

import xyz.katiedotson.restaurantsapp.data.repo.RestaurantsRepository
import xyz.katiedotson.restaurantsapp.domain.model.Restaurant
import javax.inject.Inject

class GetSortedRestaurantsUseCase @Inject constructor(private val repository: RestaurantsRepository) {

    suspend operator fun invoke(): List<Restaurant> {
        return repository.getCachedRestaurants().sortedBy { it.title }
    }

}
