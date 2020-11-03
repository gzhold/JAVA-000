package io.github.gateway.router;

import java.util.List;

public class LoadBalance{
    private String routerName;

    public LoadBalance (String routerName) {
        this.routerName = routerName;
    }

    protected HttpEndpointRouter getRouter(String routerName) {
        if ("random".equals(routerName)) {
            return new RandomRouter();
        } else if ("roundRobin".equals(routerName)) {
            return new RoundRobinRouter();
        }
        return new RandomRouter();
    }

    public String doSelect(List<String> endpoints) {
        if (null == endpoints || endpoints.size() == 0) {
            return null;
        }
//        if (endpoints.size() == 1) {
//            return endpoints.get(0);
//        }
        HttpEndpointRouter router = getRouter(routerName);
        return router.route(endpoints);
    }

}
