package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastestRouteStrategyTest {

    @Test
    void testSelectFastestRoute() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 30, 3000, 2.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 20, 4000, 1.8, true
        );

        Route r3 = new Route(
                "R3", warehouse, customer,
                15, 40, 2000, 2.5, true
        );

        List<Route> routes = List.of(r1, r2, r3);

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT,
                5000,
                60,
                false
        );

        FastestRouteStrategy strategy = new FastestRouteStrategy();

        Route result = strategy.selectRoute(routes, request);

        assertEquals("R2", result.id());
    }


    @Test
    void testUnavailableRoute() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 10, 3000, 2.0, false
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 30, 4000, 1.8, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT, 5000, 60, false
        );

        FastestRouteStrategy strategy = new FastestRouteStrategy();

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }

    @Test
    void testBudgetLimit() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 15, 6000, 2.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 30, 3000, 1.8, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT, 5000, 60, false
        );

        FastestRouteStrategy strategy = new FastestRouteStrategy();

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }

    @Test
    void testNoSuitableRoute() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 30, 6000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT, 1000, 15, false
        );

        FastestRouteStrategy strategy = new FastestRouteStrategy();

        assertThrows(
                NoSuitableRouteException.class,
                () -> strategy.selectRoute(List.of(r1), request)
        );
    }

}
