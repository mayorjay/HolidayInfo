package com.josycom.mayorjay.holidayinfo.ui.nav

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable

@Serializable
object Overview

data class Details(
    val route: String = "details",
    val yearArg: String = "year",
    val countryArg: String = "country",
    val routeWithArgs: String = "${route}/{${yearArg}}/{${countryArg}}",
    val arguments: List<NamedNavArgument> = listOf(
        navArgument(yearArg) { type = NavType.StringType },
        navArgument(countryArg) { type = NavType.StringType }
    )
)

data class PopUp(
    val route: String = "popup",
    val countryArg: String = "country",
    val routeWithArg: String = "${route}/{${countryArg}}",
    val arguments: List<NamedNavArgument> = listOf(
        navArgument(countryArg) { type = NavType.StringType }
    )
)

val details = Details()
val popUp = PopUp()