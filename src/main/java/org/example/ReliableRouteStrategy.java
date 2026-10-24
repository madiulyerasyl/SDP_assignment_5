package org.example;

import java.util.List;

public class ReliableRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException("Route list is empty");
        }

        Route bestRoute = null;
        double lowestRisk = Double.MAX_VALUE;

        for (Route route : routes) {

            // Check delivery constraints
            if (!RouteValidator.isValid(route, request)) {
                continue;
            }

            // Calculate estimated risk score
            double riskScore = route.estimatedMinutes() * 0.6
                    + route.distanceKm() * 0.4;

            if (bestRoute == null || riskScore < lowestRisk) {
                bestRoute = route;
                lowestRisk = riskScore;
            }
        }

        if (bestRoute == null) {
            throw new NoSuitableRouteException("No suitable route found");
        }

        // Explain route selection
        System.out.println();
        System.out.println("=== RELIABLE STRATEGY EXPLANATION ===");

        for (Route route : routes) {

            if (!RouteValidator.isValid(route, request)) {

                System.out.println(
                        route.id() + " rejected: "
                                + RouteValidator.getRejectionReason(route, request)
                );

                continue;
            }

            double riskScore = route.estimatedMinutes() * 0.6
                    + route.distanceKm() * 0.4;

            if (route == bestRoute) {

                System.out.printf(
                        "%s selected: lowest Risk Score = %.2f%n",
                        route.id(), riskScore
                );

            } else {

                System.out.printf(
                        "%s not selected: Risk Score = %.2f, higher than %s%n",
                        route.id(), riskScore, bestRoute.id()
                );
            }
        }

        return bestRoute;
    }

    @Override
    public String getStrategyName() {
        return "Reliable Route Strategy";
    }
}
