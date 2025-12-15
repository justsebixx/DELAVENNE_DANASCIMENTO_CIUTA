package fr.upjv;

import fr.upjv.simple.PrintListener;
import fr.upjv.simple.SimplePublisher;

public class Demo {
    public static void main(String[] args) {
        SimplePublisher pub = new SimplePublisher();
        PrintListener l1 = new PrintListener("Theo");
        PrintListener l2 = new PrintListener("paul");
        PrintListener l3 = new PrintListener("jack");

        pub.subscribe("sport", l1);
        pub.subscribe("sport", l2);
        pub.subscribe("news", l2);
        pub.subscribe("news", l3);

        pub.publish("sport", "Hello World");
        pub.publish("news", "test 2");
    }
}
