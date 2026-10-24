
package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StrategySelectorTest {

    @Test
    void testUrgentPriority() {

        StrategySelector selector = new StrategySelector();

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.URGENT, 5000, 40, false
        );

        RouteStrategy strategy = selector.selectStrategy(request);

        assertInstanceOf(FastestRouteStrategy.class, strategy);
    }

    @Test
    void testEconomyPriority() {

        StrategySelector selector = new StrategySelector();

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECONOMY, 5000, 40, false
        );

        RouteStrategy strategy = selector.selectStrategy(request);

        assertInstanceOf(CheapestRouteStrategy.class, strategy);
    }

    @Test
    void testEcoPriority() {

        StrategySelector selector = new StrategySelector();

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.ECO, 5000, 40, true
        );

        RouteStrategy strategy = selector.selectStrategy(request);

        assertInstanceOf(EcoFriendlyRouteStrategy.class, strategy);
    }

    @Test
    void testStandardPriority() {

        StrategySelector selector = new StrategySelector();

        DeliveryRequest request = new DeliveryRequest(
                DeliveryPriority.STANDARD, 5000, 40, false
        );

        RouteStrategy strategy = selector.selectStrategy(request);

        assertInstanceOf(BalancedRouteStrategy.class, strategy);
    }

    @Test
    void testNullRequest() {

        StrategySelector selector = new StrategySelector();

        assertThrows(
                IllegalArgumentException.class,
                () -> selector.selectStrategy(null)
        );
    }
}
