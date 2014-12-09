package com.luxoft.akkalabs.clients;

import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    public static String get(String key) {
        return properties.getProperty(key);
    }

    static {
        properties.put("twitter.customer.key", "HGRrWKDaCv5ox9KYExhnvYKjj");
        properties.put("twitter.customer.secret", "OXqQ2yp2NGIMD6MQdGyPJ92XhknpWE8HRpDLxGGYmDX5lc82lY");
        properties.put("twitter.access.token", "25685469-a8GzmAV5zBwSmSuxsOf7pdsRbzQj9bD0GTqf9185W");
        properties.put("twitter.access.secret", "B103NdJ41zpsQvQL1eVIyKy0WPn376vBT8z1MCkkavDBL");
    }
}
