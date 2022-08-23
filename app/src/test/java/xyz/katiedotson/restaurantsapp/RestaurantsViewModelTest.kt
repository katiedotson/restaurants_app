package xyz.katiedotson.restaurantsapp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import xyz.katiedotson.restaurantsapp.data.repo.RestaurantsRepository
import xyz.katiedotson.restaurantsapp.domain.usecase.GetInitialRestaurantsUseCase
import xyz.katiedotson.restaurantsapp.domain.usecase.GetSortedRestaurantsUseCase
import xyz.katiedotson.restaurantsapp.domain.usecase.ToggleRestaurantUseCase
import xyz.katiedotson.restaurantsapp.features.restaurantslist.RestaurantsScreenState
import xyz.katiedotson.restaurantsapp.features.restaurantslist.RestaurantsViewModel
import xyz.katiedotson.restaurantsapp.testutil.DummyContent

@ExperimentalCoroutinesApi
class RestaurantsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun initialState_isProduced() = scope.runTest {
        val viewModel = getViewModel()
        val initialState = viewModel.state.value
        assert(
            initialState == RestaurantsScreenState(
                restaurants = emptyList(),
                isLoading = true,
                error = null
            )
        )
    }

    @Test
    fun stateWithContent_isProduced() = scope.runTest {
        val testVM = getViewModel()
        advanceUntilIdle()
        val currentState = testVM.state.value
        assert(
            currentState == RestaurantsScreenState(
                restaurants = DummyContent.getDomainRestaurants(),
                isLoading = false,
                error = null
            )
        )
    }

    private fun getViewModel(): RestaurantsViewModel {

        val restaurantsRepository = RestaurantsRepository(restaurantsDao = DummyDao(), service = DummyApiService(), dispatcher = dispatcher)

        val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)

        val getInitialRestaurantsUseCase = GetInitialRestaurantsUseCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        val toggleRestaurantUseCase = ToggleRestaurantUseCase(
            restaurantsRepository,
            getSortedRestaurantsUseCase
        )

        return RestaurantsViewModel(
            getInitialRestaurantsUseCase,
            toggleRestaurantUseCase,
            dispatcher
        )

    }

}
