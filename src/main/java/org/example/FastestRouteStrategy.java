
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

            // Check delivery constraints
            if (!RouteValidator.isValid(route, request)) {
                continue;
            }

            // Select the fastest valid route
            if (fastest == null ||
                    route.estimatedMinutes() < fastest.estimatedMinutes()) {
                fastest = route;
            }
        }


        if (fastest == null) {
            throw new NoSuitableRouteException("No suitable route found");
        }

// Explain the selected route
        System.out.println();
        System.out.println("=== FASTEST STRATEGY EXPLANATION ===");

        for (Route route : routes) {

            if (!RouteValidator.isValid(route, request)) {

                System.out.println(
                        route.id() + " rejected: "
                                + RouteValidator.getRejectionReason(route, request)
                );

            } else if (route == fastest) {

                System.out.println(
                        route.id() + " selected: fastest valid route ("
                                + route.estimatedMinutes() + " minutes)"
                );

            } else {

                System.out.println(
                        route.id() + " not selected: slower than "
                                + fastest.id()
                );
            }
        }

        return fastest;

    }

    @Override
    public String getStrategyName() {
        return "Fastest Route Strategy";
    }
}
