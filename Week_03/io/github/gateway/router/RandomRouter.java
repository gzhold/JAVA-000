package io.github.gateway.router;

import java.util.List;
import java.util.Random;


/**
 * 随机
 */
public class RandomRouter implements HttpEndpointRouter {
    private final Random random = new Random();

    @Override
    public String route(List<String> endpoints) {
        System.out.println("Random access....");
        return endpoints.get(random.nextInt(endpoints.size()));
    }
}
