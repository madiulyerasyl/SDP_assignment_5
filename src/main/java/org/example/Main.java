package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);



        Route r1 = new Route("R1", warehouse, customer, 10, 20, 6000, 2.0, true);
        Route r2 = new Route("R2", warehouse, customer, 12, 30, 3500, 1.8, true);
        Route r3 = new Route("R3", warehouse, customer, 15, 45, 2000, 2.5, true);
        Route r4 = new Route("R4", warehouse, customer, 14, 35, 3000, 1.5, true);

// Additional routes
        Route r5 = new Route("R5", warehouse, customer, 9, 18, 4500, 2.2, true);
        Route r6 = new Route("R6", warehouse, customer, 16, 38, 2500, 1.2, true);
        Route r7 = new Route("R7", warehouse, customer, 11, 25, 4000, 1.0, false);
        Route r8 = new Route("R8", warehouse, customer, 13, 32, 3200, 1.4, true);



        List<Route> routes = List.of(r1, r2, r3, r4, r5, r6, r7, r8);


        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD,
                5000,
                40,
                false
        );

// Automatically select strategy
        StrategySelector selector = new StrategySelector();

        RouteStrategy selectedStrategy = selector.selectStrategy(request);

// Create RoutePlanner with selected strategy
        RoutePlanner planner = new RoutePlanner(selectedStrategy);

// Find the best route
        Route result = planner.plan(routes, request);

        System.out.println("Priority: " + request.priority());
        System.out.println("Selected Strategy: " + selectedStrategy.getStrategyName());
        System.out.println("Selected Route: " + result.id());
        System.out.println("Time: " + result.estimatedMinutes() + " minutes");
        System.out.println("Cost: " + result.cost());


        System.out.println();
        System.out.println("=== STRATEGY COMPARISON ===");

        RouteStrategy[] strategies = {
                new FastestRouteStrategy(),
                new CheapestRouteStrategy(),
                new EcoFriendlyRouteStrategy(),
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2),
                new ReliableRouteStrategy()
        };


        for (RouteStrategy strategy : strategies) {

            planner.setStrategy(strategy);

            try {
                Route selectedRoute = planner.plan(routes, request);

                System.out.println(
                        strategy.getStrategyName()
                                + " -> " + selectedRoute.id()
                                + " | Time: " + selectedRoute.estimatedMinutes()
                                + " min | Cost: " + selectedRoute.cost()
                );

            } catch (NoSuitableRouteException e) {
                System.out.println(
                        strategy.getStrategyName()
                                + " -> " + e.getMessage()
                );
            }
        }


        System.out.println();
        System.out.println("=== ROUTE VALIDATION REPORT ===");

        for (Route route : routes) {

            String reason = RouteValidator.getRejectionReason(route, request);

            System.out.println(route.id() + " -> " + reason);
        }


    }
}
