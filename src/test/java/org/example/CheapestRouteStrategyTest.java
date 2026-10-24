
package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheapestRouteStrategyTest {

    @Test
    void testSelectCheapestRoute() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 4000, 2.0, true
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                15, 30, 2000, 1.8, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 5000, 60, false
        );

        CheapestRouteStrategy strategy = new CheapestRouteStrategy();

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }

    @Test
    void testTotalCostExceedsBudget() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                20, 30, 3000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 3500, 60, false
        );

        CheapestRouteStrategy strategy = new CheapestRouteStrategy();

        assertThrows(
                NoSuitableRouteException.class,
                () -> strategy.selectRoute(List.of(r1), request)
        );
    }

    @Test
    void testUrgentPriorityFee() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 3000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT, 3800, 60, false
        );

        CheapestRouteStrategy strategy = new CheapestRouteStrategy();

        assertThrows(
                NoSuitableRouteException.class,
                () -> strategy.selectRoute(List.of(r1), request)
        );
    }

    @Test
    void testCheapestRouteUnavailable() {

        Location warehouse = new Location("Warehouse", 51.16, 71.43);
        Location customer = new Location("Customer", 51.18, 71.45);

        Route r1 = new Route(
                "R1", warehouse, customer,
                10, 20, 1000, 2.0, false
        );

        Route r2 = new Route(
                "R2", warehouse, customer,
                12, 30, 3000, 1.8, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 5000, 60, false
        );

        CheapestRouteStrategy strategy = new CheapestRouteStrategy();

        Route result = strategy.selectRoute(List.of(r1, r2), request);

        assertEquals("R2", result.id());
    }
}
