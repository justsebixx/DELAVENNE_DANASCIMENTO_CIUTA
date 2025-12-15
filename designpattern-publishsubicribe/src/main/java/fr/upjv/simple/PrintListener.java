package fr.upjv.simple;

public class PrintListener implements SimpleListener {
    private final String name;

    public PrintListener(String name) {
        this.name = name;
    }

    @Override
    public void onMessage(String topic, String message) {
        System.out.println("[" + name + "] à reçu du topic : '" + topic + "' le message : " + message);
    }
}

