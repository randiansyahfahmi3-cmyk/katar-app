package com.katar.app.navigation

enum class Route {
    BERANDA,
    ANGGOTA,
    KAS,
    AGENDA
}

/** String route destinations for NavController */
val routeString: Route.() -> String get() = { "${this}" }