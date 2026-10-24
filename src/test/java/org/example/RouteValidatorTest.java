package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteValidatorTest {

    Location warehouse = new Location("Warehouse", 51.16, 71.43);
    Location customer = new Location("Customer", 51.18, 71.45);

    @Test
    void testValidRoute() {

        Route route = new Route(
                "R1", warehouse, customer,
                10, 30, 3000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        assertTrue(RouteValidator.isValid(route, request));
    }

    @Test
    void testUnavailableRoute() {

        Route route = new Route(
                "R1", warehouse, customer,
                10, 30, 3000, 2.0, false
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        assertFalse(RouteValidator.isValid(route, request));
    }

    @Test
    void testRouteExceedsBudget() {

        Route route = new Route(
                "R1", warehouse, customer,
                10, 30, 6000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        assertFalse(RouteValidator.isValid(route, request));
    }

    @Test
    void testRouteExceedsDeadline() {

        Route route = new Route(
                "R1", warehouse, customer,
                10, 90, 3000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        assertFalse(RouteValidator.isValid(route, request));
    }

    @Test
    void testInvalidRouteData() {

        Route route = new Route(
                "R1", warehouse, customer,
                -10, 30, 3000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 60, false
        );

        assertFalse(RouteValidator.isValid(route, request));
    }

    @Test
    void testExactBudgetAndDeadlineBoundary() {

        Route route = new Route(
                "R1", warehouse, customer,
                10, 40, 5000, 2.0, true
        );

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 40, false
        );

        assertTrue(RouteValidator.isValid(route, request));
    }

}
