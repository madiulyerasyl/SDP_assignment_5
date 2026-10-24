
package org.example;

import java.util.EnumMap;
import java.util.Map;

public class StrategySelector {

    private final Map<DeliveryPriority, RouteStrategy> strategies =
            new EnumMap<>(DeliveryPriority.class);

    public StrategySelector() {
        strategies.put(DeliveryPriority.URGENT,
                new FastestRouteStrategy());

        strategies.put(DeliveryPriority.ECONOMY,
                new CheapestRouteStrategy());

        strategies.put(DeliveryPriority.ECO,
                new EcoFriendlyRouteStrategy());

        strategies.put(DeliveryPriority.STANDARD,
                new BalancedRouteStrategy(0.4, 0.3, 0.1, 0.2));
    }

    public RouteStrategy selectStrategy(DeliveryRequest request) {

        if (request == null || request.priority() == null) {
            throw new IllegalArgumentException("Invalid delivery request");
        }

        RouteStrategy strategy = strategies.get(request.priority());

        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found");
        }

        return strategy;
    }

    public void registerStrategy(
            DeliveryPriority priority,
            RouteStrategy strategy) {

        if (priority == null || strategy == null) {
            throw new IllegalArgumentException("Invalid strategy");
        }

        strategies.put(priority, strategy);
    }
}
