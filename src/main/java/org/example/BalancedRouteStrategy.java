package org.example;

import java.util.List;

public class BalancedRouteStrategy implements RouteStrategy {

    private double timeWeight;
    private double costWeight;
    private double distanceWeight;
    private double emissionWeight;

    public BalancedRouteStrategy(
            double timeWeight,
            double costWeight,
            double distanceWeight,
            double emissionWeight) {

        double sum = timeWeight + costWeight
                + distanceWeight + emissionWeight;

        if (!Double.isFinite(sum)
                || !Double.isFinite(timeWeight)
                || !Double.isFinite(costWeight)
                || !Double.isFinite(distanceWeight)
                || !Double.isFinite(emissionWeight)
                || timeWeight < 0 || costWeight < 0
                || distanceWeight < 0 || emissionWeight < 0
                || Math.abs(sum - 1.0) > 0.000001) {

            throw new IllegalArgumentException("Invalid weights");
        }

        this.timeWeight = timeWeight;
        this.costWeight = costWeight;
        this.distanceWeight = distanceWeight;
        this.emissionWeight = emissionWeight;
    }

    @Override
    public Route selectRoute(List<Route> routes, DeliveryRequest request) {

        if (routes == null || routes.isEmpty()) {
            throw new IllegalArgumentException("Route list is empty");
        }

        if (request == null) {
            throw new IllegalArgumentException("Request is missing");
        }

        Route bestRoute = null;
        double lowestScore = Double.MAX_VALUE;

        double maxTime = 0;
        double maxCost = 0;
        double maxDistance = 0;
        double maxEmission = 0;

        // Find maximum values for normalization
        for (Route route : routes) {
            maxTime = Math.max(maxTime, route.estimatedMinutes());
            maxCost = Math.max(maxCost, route.cost());
            maxDistance = Math.max(maxDistance, route.distanceKm());
            maxEmission = Math.max(maxEmission, route.carbonEmission());
        }

        for (Route route : routes) {

            // Check delivery constraints
            if (route.cost() > request.maxBudget()) {
                continue;
            }

            if (route.estimatedMinutes() > request.maxDeliveryMinutes()) {
                continue;
            }

            // Normalize values
            double time = maxTime == 0 ? 0 :
                    route.estimatedMinutes() / maxTime;

            double cost = maxCost == 0 ? 0 :
                    route.cost() / maxCost;

            double distance = maxDistance == 0 ? 0 :
                    route.distanceKm() / maxDistance;

            double emission = maxEmission == 0 ? 0 :
                    route.carbonEmission() / maxEmission;

            // Calculate balanced score
            double score =
                    time * timeWeight
                            + cost * costWeight
                            + distance * distanceWeight
                            + emission * emissionWeight;

            if (score < lowestScore) {
                lowestScore = score;
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
        return "Balanced Route Strategy";
    }
}
