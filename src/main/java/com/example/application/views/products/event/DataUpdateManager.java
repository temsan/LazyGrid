package com.example.application.views.products.event;

import com.example.application.views.products.ListProducts;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataUpdateManager <T extends DataUpdateListener> {
    private static final List<ListProducts> listeners = new ArrayList<>();

    public static void addListener(ListProducts listener) {
        listeners.add(listener);
    }

    public static void removeListener(ListProducts listener) {
        listeners.remove(listener);
    }

    public static void notifyListeners() {
        for (ListProducts listener : listeners) {
            listener.dataUpdated();
        }
    }
}

