package org.example;

import java.util.List;

public class BalancedRouteStrategy implements RouteStrategy {

    private final double timeWeight;
    private final double costWeight;
    private final double distanceWeight;
    private final double emissionWeight;

    public BalancedRouteStrategy(double timeWeight, double costWeight,
                                 double distanceWeight, double emissionWeight) {

        if (!Double.isFinite(timeWeight)
                || !Double.isFinite(costWeight)
                || !Double.isFinite(distanceWeight)
                || !Double.isFinite(emissionWeight)
                || timeWeight < 0 || costWeight < 0
                || distanceWeight < 0 || emissionWeight < 0
                || Math.abs(timeWeight + costWeight
                + distanceWeight + emissionWeight - 1.0) > 0.000001) {

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

        double maxTime = 0;
        double maxCost = 0;
        double maxDistance = 0;
        double maxEmission = 0;

        // Find maximum values among valid routes
        for (Route route : routes) {

            if (!RouteValidator.isValid(route, request)) {
                continue;
            }

            maxTime = Math.max(maxTime, route.estimatedMinutes());
            maxCost = Math.max(maxCost, route.cost());
            maxDistance = Math.max(maxDistance, route.distanceKm());
            maxEmission = Math.max(maxEmission, route.carbonEmission());
        }

        Route bestRoute = null;
        double lowestScore = Double.MAX_VALUE;

        // Calculate balanced score
        for (Route route : routes) {

            if (!RouteValidator.isValid(route, request)) {
                continue;
            }

            double timeScore = maxTime == 0 ? 0
                    : route.estimatedMinutes() / maxTime;

            double costScore = maxCost == 0 ? 0
                    : route.cost() / maxCost;

            double distanceScore = maxDistance == 0 ? 0
                    : route.distanceKm() / maxDistance;

            double emissionScore = maxEmission == 0 ? 0
                    : route.carbonEmission() / maxEmission;

            double totalScore =
                    timeScore * timeWeight
                            + costScore * costWeight
                            + distanceScore * distanceWeight
                            + emissionScore * emissionWeight;

            if (bestRoute == null || totalScore < lowestScore) {
                bestRoute = route;
                lowestScore = totalScore;
            }
        }


        if (bestRoute == null) {
            throw new NoSuitableRouteException("No suitable route found");
        }

// Explain balanced route selection
        System.out.println();
        System.out.println("=== BALANCED STRATEGY EXPLANATION ===");

        for (Route route : routes) {

            if (!RouteValidator.isValid(route, request)) {

                System.out.println(
                        route.id() + " rejected: "
                                + RouteValidator.getRejectionReason(route, request)
                );

                continue;
            }

            // Normalize route values
            double timeScore = maxTime == 0 ? 0
                    : route.estimatedMinutes() / maxTime;

            double costScore = maxCost == 0 ? 0
                    : route.cost() / maxCost;

            double distanceScore = maxDistance == 0 ? 0
                    : route.distanceKm() / maxDistance;

            double emissionScore = maxEmission == 0 ? 0
                    : route.carbonEmission() / maxEmission;

            // Calculate weighted score
            double totalScore =
                    timeScore * timeWeight
                            + costScore * costWeight
                            + distanceScore * distanceWeight
                            + emissionScore * emissionWeight;

            if (route == bestRoute) {

                System.out.printf(
                        "%s selected: lowest Balanced Score = %.4f%n",
                        route.id(), totalScore
                );

            } else {

                System.out.printf(
                        "%s not selected: Balanced Score = %.4f, higher than %s%n",
                        route.id(), totalScore, bestRoute.id()
                );
            }
        }

        return bestRoute;

    }

    @Override
    public String getStrategyName() {
        return "Balanced Route Strategy";
    }
}
