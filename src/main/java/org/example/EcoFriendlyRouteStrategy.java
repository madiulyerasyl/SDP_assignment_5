package org.example;

import java.util.List;

public class EcoFriendlyRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException("Route list is empty");
        }

        Route bestRoute = null;
        double lowestEcoScore = Double.MAX_VALUE;

        for (Route route : routes) {

            // Check availability, budget and deadline
            if (!RouteValidator.isValid(route, request)) {
                continue;
            }

            // Calculate environmental score
            double ecoScore = route.carbonEmission() * 0.7
                    + route.distanceKm() * 0.3;

            // Select route with the lowest eco score
            if (bestRoute == null || ecoScore < lowestEcoScore) {
                bestRoute = route;
                lowestEcoScore = ecoScore;
            }
        }


        if (bestRoute == null) {
            throw new NoSuitableRouteException("No suitable route found");
        }

// Explain eco-friendly route selection
        System.out.println();
        System.out.println("=== ECO FRIENDLY STRATEGY EXPLANATION ===");

        for (Route route : routes) {

            // Check delivery constraints
            if (!RouteValidator.isValid(route, request)) {

                System.out.println(
                        route.id() + " rejected: "
                                + RouteValidator.getRejectionReason(route, request)
                );

                continue;
            }

            // Calculate environmental score
            double ecoScore = route.carbonEmission() * 0.7
                    + route.distanceKm() * 0.3;

            if (route == bestRoute) {

                System.out.println(
                        route.id() + " selected: lowest EcoScore = "
                                + ecoScore
                );

            } else {

                System.out.println(
                        route.id() + " not selected: EcoScore = "
                                + ecoScore + ", higher than "
                                + bestRoute.id()
                );
            }
        }

        return bestRoute;

    }

    @Override
    public String getStrategyName() {
        return "Eco Friendly Route Strategy";
    }
}
