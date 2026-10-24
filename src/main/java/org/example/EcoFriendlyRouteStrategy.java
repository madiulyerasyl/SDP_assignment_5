package org.example;

import java.util.List;

public class EcoFriendlyRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException("Route list is empty");
        }

        Route bestRoute = null;
        double lowestScore = Double.MAX_VALUE;

        double emissionWeight = 0.7;
        double distanceWeight = 0.3;

        for (Route route : routes) {

            // Check budget
            if (route.cost() > request.maxBudget()) {
                continue;
            }

            // Check delivery deadline
            if (route.estimatedMinutes() > request.maxDeliveryMinutes()) {
                continue;
            }

            // Calculate environmental score
            double ecoScore =
                    route.carbonEmission() * emissionWeight
                            + route.distanceKm() * distanceWeight;

            // Find the route with the lowest eco score
            if (ecoScore < lowestScore) {
                lowestScore = ecoScore;
                bestRoute = route;
            }
        }

        if (bestRoute == null) {
            throw new IllegalStateException("No suitable route found");
        }

        return bestRoute;
    }

    @Override
    public String getStrategyName() {
        return "Eco Friendly Route Strategy";
    }
}
