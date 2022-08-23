package xyz.katiedotson.restaurantsapp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Test
import xyz.katiedotson.restaurantsapp.data.repo.RestaurantsRepository
import xyz.katiedotson.restaurantsapp.domain.usecase.GetSortedRestaurantsUseCase
import xyz.katiedotson.restaurantsapp.domain.usecase.ToggleRestaurantUseCase
import xyz.katiedotson.restaurantsapp.testutil.DummyContent

@ExperimentalCoroutinesApi
class ToggleRestaurantUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun toggleRestaurant_IsUpdatingFavoriteField() = scope.runTest {
        // Setup useCase
        val restaurantsRepository = RestaurantsRepository(
            DummyApiService(),
            DummyDao(),
            dispatcher
        )
        val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)
        val useCase = ToggleRestaurantUseCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        // Preload data
        restaurantsRepository.loadRestaurants()
        advanceUntilIdle()

        // Execute useCase
        val restaurants = DummyContent.getDomainRestaurants()
        val targetItem = restaurants[0]
        val isFavorite = targetItem.isFavorite
        val updatedRestaurants = useCase(targetItem.id, isFavorite)
        advanceUntilIdle()

        // Assertion
        restaurants[0] = targetItem.copy(isFavorite = !isFavorite)
        assert(updatedRestaurants == restaurants)
    }
}
