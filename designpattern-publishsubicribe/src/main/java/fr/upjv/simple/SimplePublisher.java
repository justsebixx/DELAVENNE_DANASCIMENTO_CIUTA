package fr.upjv.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePublisher {
    private final Map<String, List<SimpleListener>> topics = new HashMap<>();

    public void subscribe(String topic, SimpleListener listener) {
        topics.putIfAbsent(topic, new ArrayList<>());
        topics.get(topic).add(listener);
    }



    public void publish(String topic, String message) {
        if (topics.containsKey(topic)) {
            for (SimpleListener l : topics.get(topic)) {
                l.onMessage(topic, message);
            }
        }
    }
}

