
package org.example;

public class RouteValidator {

    public static boolean isValid(Route route, DeliveryRequest request) {

        if (route == null || request == null) {
            return false;
        }

        // Check route availability
        if (!route.available()) {
            return false;
        }

        // Check invalid route data
        if (!Double.isFinite(route.cost())
                || !Double.isFinite(route.distanceKm())
                || !Double.isFinite(route.carbonEmission())
                || route.cost() < 0
                || route.distanceKm() < 0
                || route.carbonEmission() < 0
                || route.estimatedMinutes() < 0) {
            return false;
        }

        // Check request limits
        if (!Double.isFinite(request.maxBudget())
                || request.maxBudget() < 0
                || request.maxDeliveryMinutes() < 0) {
            return false;
        }

        // Check budget
        if (route.cost() > request.maxBudget()) {
            return false;
        }

        // Check delivery deadline
        if (route.estimatedMinutes() > request.maxDeliveryMinutes()) {
            return false;
        }

        return true;
    }


    public static String getRejectionReason(Route route, DeliveryRequest request) {

        if (route == null) {
            return "Route is null";
        }

        if (request == null) {
            return "Delivery request is null";
        }

        if (!route.available()) {
            return "Route is unavailable";
        }

        if (!Double.isFinite(route.cost())
                || !Double.isFinite(route.distanceKm())
                || !Double.isFinite(route.carbonEmission())
                || route.cost() < 0
                || route.distanceKm() < 0
                || route.carbonEmission() < 0
                || route.estimatedMinutes() < 0) {
            return "Invalid route data";
        }

        if (!Double.isFinite(request.maxBudget())
                || request.maxBudget() < 0
                || request.maxDeliveryMinutes() < 0) {
            return "Invalid delivery limits";
        }

        if (route.cost() > request.maxBudget()) {
            return "Route exceeds budget";
        }

        if (route.estimatedMinutes() > request.maxDeliveryMinutes()) {
            return "Route exceeds delivery deadline";
        }

        return "Route is valid";
    }

}
