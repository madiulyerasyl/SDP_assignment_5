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

            double distanceCost = route.distanceKm() * 50;

            double priorityFee = 0;

            if (request.priority() == DeliveryPriority.URGENT) {
                priorityFee = 500;
            }

            double totalCost = route.cost() + distanceCost + priorityFee;

            // Check budget
            if (totalCost > request.maxBudget()) {
                continue;
            }

            // Check delivery deadline
            if (route.estimatedMinutes() > request.maxDeliveryMinutes()) {
                continue;
            }

            // Find the cheapest valid route
            if (totalCost < lowestCost) {
                lowestCost = totalCost;
                cheapest = route;
            }
        }

        if (cheapest == null) {
            throw new IllegalStateException("No suitable route found");
        }

        return cheapest;
    }

    @Override
    public String getStrategyName() {
        return "Cheapest Route Strategy";
    }
}
