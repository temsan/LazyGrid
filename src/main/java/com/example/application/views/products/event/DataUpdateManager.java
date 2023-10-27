package com.example.application.views.products.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("singleton")
public class DataUpdateManager<T extends DataUpdateListener> {
    private final List<T> listeners = new ArrayList<>();


    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (T listener : listeners) {
            listener.dataUpdated();
        }
    }
}

