package se.iwoio.project.datastoredemo.ui.navigation

sealed class MainNavigation(val route: String) {
    object MainTodoScreen : MainNavigation("maintodoscreen")
}

