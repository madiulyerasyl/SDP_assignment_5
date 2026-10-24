package org.example;

import java.util.List;

public class CheapestRouteStrategy implements RouteStrategy {

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException("Route list is empty");
        }

        Route cheapest = null;
        double lowestCost = Double.MAX_VALUE;

        for (Route route : routes) {

            // Check route availability, budget and deadline
            if (!RouteValidator.isValid(route, request)) {
                continue;
            }

            // Calculate delivery cost
            double distanceCost = route.distanceKm() * 50;

            double priorityFee = 0;

            if (request.priority() == DeliveryPriority.URGENT) {
                priorityFee = 500;
            }

            double totalCost = route.cost() + distanceCost + priorityFee;

            // Check the final delivery cost
            if (totalCost > request.maxBudget()) {
                continue;
            }

            // Find the cheapest route
            if (cheapest == null || totalCost < lowestCost) {
                cheapest = route;
                lowestCost = totalCost;
            }
        }


        if (cheapest == null) {
            throw new NoSuitableRouteException("No suitable route found");
        }

// Explain route selection
        System.out.println();
        System.out.println("=== CHEAPEST STRATEGY EXPLANATION ===");

        for (Route route : routes) {

            // Check general delivery constraints
            if (!RouteValidator.isValid(route, request)) {

                System.out.println(
                        route.id() + " rejected: "
                                + RouteValidator.getRejectionReason(route, request)
                );

                continue;
            }

            // Calculate full delivery cost
            double distanceCost = route.distanceKm() * 50;

            double priorityFee = 0;

            if (request.priority() == DeliveryPriority.URGENT) {
                priorityFee = 500;
            }

            double totalCost = route.cost() + distanceCost + priorityFee;

            if (totalCost > request.maxBudget()) {

                System.out.println(
                        route.id() + " rejected: total cost "
                                + totalCost + " exceeds budget "
                                + request.maxBudget()
                );

            } else if (route == cheapest) {

                System.out.println(
                        route.id() + " selected: lowest total cost = "
                                + totalCost
                );

            } else {

                System.out.println(
                        route.id() + " not selected: total cost = "
                                + totalCost + ", more expensive than "
                                + cheapest.id()
                );
            }
        }

        return cheapest;

    }

    @Override
    public String getStrategyName() {
        return "Cheapest Route Strategy";
    }
}
