package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EcoFriendlyRouteStrategyTest {

    @Test
    void testSelectLowestEcoScore() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 30, 3000, 3.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                8, 35, 3500, 1.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 60, true
        );

        EcoFriendlyRouteStrategy strategy = new EcoFriendlyRouteStrategy();

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }

    @Test
    void testEcoRouteUnavailable() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                5, 20, 2000, 0.5, false
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                10, 30, 3000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 60, true
        );

        EcoFriendlyRouteStrategy strategy = new EcoFriendlyRouteStrategy();

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }

    @Test
    void testEcoRouteExceedsDeadline() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                5, 70, 2000, 0.5, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 40, true
        );

        EcoFriendlyRouteStrategy strategy = new EcoFriendlyRouteStrategy();

        assertThrows(
                NoSuitableRouteException.class,
                () -> strategy.selectRoute(List.of(r1), request)
        );
    }
}
