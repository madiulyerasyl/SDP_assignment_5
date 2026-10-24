package org.example;

import java.util.List;

public class FastestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException("Route list is empty");
        }

        Route fastest = null;

        for (Route route : routes) {

            // Check budget
            if (route.cost() > request.maxBudget()) {
                continue;
            }

            // Check delivery deadline
            if (route.estimatedMinutes() > request.maxDeliveryMinutes()) {
                continue;
            }

            // Find the fastest valid route
            if (fastest == null ||
                    route.estimatedMinutes() < fastest.estimatedMinutes()) {
                fastest = route;
            }
        }

        if (fastest == null) {
            throw new IllegalStateException("No suitable route found");
        }

        return fastest;
    }

    @Override
    public String getStrategyName() {
        return "Fastest Route Strategy";
    }
}
