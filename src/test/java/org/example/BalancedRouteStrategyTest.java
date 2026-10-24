
package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BalancedRouteStrategyTest {

    @Test
    void testBalancedRouteSelection() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 3000, 1.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                20, 40, 5000, 3.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 6000, 60, false
        );

        BalancedRouteStrategy strategy =
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2);

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R1", result.id());
    }

    @Test
    void testBalancedRouteUnavailable() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                5, 10, 1000, 0.5, false
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 30, 3000, 1.8, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        BalancedRouteStrategy strategy =
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2);

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }

    @Test
    void testInvalidWeights() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BalancedRouteStrategy(0.5, 0.5, 0.5, 0.5)
        );
    }

    @Test
    void testBalancedNoSuitableRoute() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 30, 6000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 3000, 20, false
        );

        BalancedRouteStrategy strategy =
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2);

        assertThrows(
                NoSuitableRouteException.class,
                () -> strategy.selectRoute(List.of(r1), request)
        );
    }
}
