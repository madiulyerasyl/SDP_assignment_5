package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route("R1", warehouse, customer, 10, 20, 6000, 2.0);
        Route r2 = new Route("R2", warehouse, customer, 12, 30, 3500, 1.8);
        Route r3 = new Route("R3", warehouse, customer, 15, 45, 2000, 2.5);
        Route r4 = new Route("R4", warehouse, customer, 14, 35, 3000, 1.5);

        List<Route> routes = List.of(r1, r2, r3, r4);


        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT,
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


    }
}
