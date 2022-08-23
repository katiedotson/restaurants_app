package xyz.katiedotson.restaurantsapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.*
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

import org.junit.Rule
import org.junit.Test
import xyz.katiedotson.restaurantsapp.features.restaurantslist.RestaurantsScreen
import xyz.katiedotson.restaurantsapp.features.restaurantslist.RestaurantsScreenState
import xyz.katiedotson.restaurantsapp.presentation.ContentDescription
import xyz.katiedotson.restaurantsapp.testutil.DummyContent
import xyz.katiedotson.restaurantsapp.ui.theme.RestaurantsAppTheme

class RestaurantsScreenTest {

    @get:Rule
    val testRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun initialState_isRendered() {
        testRule.setContent {
            RestaurantsAppTheme {
                RestaurantsScreen(
                    state = RestaurantsScreenState(
                        restaurants = emptyList(),
                        isLoading = true
                    ),
                    onFavoriteClick = { _: Int, _: Boolean -> },
                    onItemClick = { }
                )
            }
        }
        testRule.onNodeWithContentDescription(
            ContentDescription.RESTAURANTS_LOADING
        ).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {

        val restaurants = DummyContent.getDomainRestaurants()

        testRule.setContent {
            RestaurantsAppTheme {
                RestaurantsScreen(
                    state = RestaurantsScreenState(
                        restaurants = restaurants,
                        isLoading = false
                    ),
                    onFavoriteClick = { _: Int, _: Boolean -> },
                    onItemClick = { }
                )
            }
        }
        testRule.onNodeWithText(restaurants[0].title).assertIsDisplayed()
        testRule.onNodeWithText(restaurants[0].description).assertIsDisplayed()
        testRule.onNodeWithContentDescription(ContentDescription.RESTAURANTS_LOADING).assertDoesNotExist()
    }

    @Test
    fun stateWithContent_ClickOnItem_isRegistered() {
        val restaurants = DummyContent.getDomainRestaurants()
        val targetRestaurant = restaurants[0]
        testRule.setContent {
            RestaurantsAppTheme {
                RestaurantsScreen(
                    state = RestaurantsScreenState(
                        restaurants = restaurants,
                        isLoading = false
                    ),
                    onFavoriteClick = { _, _ -> },
                    onItemClick = { id -> assert(id == targetRestaurant.id) })
            }
        }
        testRule.onNodeWithText(targetRestaurant.title).performClick()
    }

}
