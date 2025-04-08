package com.josycom.mayorjay.holidayinfo.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.josycom.mayorjay.holidayinfo.details.DetailsScreen
import com.josycom.mayorjay.holidayinfo.details.DetailsViewModel
import com.josycom.mayorjay.holidayinfo.overview.OverviewScreen
import com.josycom.mayorjay.holidayinfo.overview.OverviewViewModel
import com.josycom.mayorjay.holidayinfo.ui.YearPopupDialog

@Composable
fun HolidayInfoNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val overviewViewModel: OverviewViewModel = hiltViewModel()
    val detailsViewModel: DetailsViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Overview,
        modifier = modifier
    ) {
        composable<Overview> {
            OverviewScreen(viewModel = overviewViewModel) { country ->
                navController.navigateToScreen("${popUp.route}/${country}")
            }
        }

        dialog(
            route = popUp.routeWithArg,
            arguments = popUp.arguments
        ) { navBackStackEntry ->
            val country = navBackStackEntry.getData(popUp.countryArg)
            YearPopupDialog(yearList = overviewViewModel.yearList) { year ->
                navController.navigateToScreen("${details.route}/${year}/${country}")
            }
        }

        composable(
            route = details.routeWithArgs,
            arguments = details.arguments
        ) { navBackStackEntry ->
            val country = navBackStackEntry.getData(details.countryArg)
            val year = navBackStackEntry.getData(details.yearArg)
            DetailsScreen(viewModel = detailsViewModel, country = country, year = year)
        }
    }
}

fun NavHostController.navigateToScreen(route: String) = this.navigate(route) { launchSingleTop = true }

fun NavBackStackEntry.getData(arg: String) = this.arguments?.getString(arg).orEmpty()