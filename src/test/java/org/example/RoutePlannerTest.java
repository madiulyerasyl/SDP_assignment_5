
package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoutePlannerTest {

    @Test
    void testPlannerWithFastestStrategy() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 4000, 2.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 35, 2000, 1.5, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 6000, 60, false
        );

        RoutePlanner planner = new RoutePlanner(
                new FastestRouteStrategy()
        );

        Route result = planner.plan(List.of(r1, r2), request);

        assertEquals("R1", result.id());
    }

    @Test
    void testRuntimeStrategySwitching() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 4000, 2.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 35, 2000, 1.5, true
        );

        List<Route> routes = List.of(r1, r2);

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 6000, 60, false
        );

        RoutePlanner planner = new RoutePlanner(
                new FastestRouteStrategy()
        );

        Route fastest = planner.plan(routes, request);

        planner.setStrategy(new CheapestRouteStrategy());

        Route cheapest = planner.plan(routes, request);

        assertEquals("R1", fastest.id());
        assertEquals("R2", cheapest.id());
    }

    @Test
    void testPlannerWithReliableStrategy() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 4000, 2.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                15, 40, 2000, 1.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 6000, 60, false
        );

        RoutePlanner planner = new RoutePlanner(
                new ReliableRouteStrategy()
        );

        Route result = planner.plan(List.of(r1, r2), request);

        assertEquals("R1", result.id());
    }
}
