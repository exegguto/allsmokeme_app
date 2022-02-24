package com.example.allsmokeme.rentlogistic

class RouteResponse {

    var routes: List<Route>? = null

    fun getPoints(): String? {
        return routes!![0].overview_polyline!!.points
    }

    class Route {
        var overview_polyline: OverviewPolyline? = null
    }

    class OverviewPolyline {
        var points: String? = null
    }
}