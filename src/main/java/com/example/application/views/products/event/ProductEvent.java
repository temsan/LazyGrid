package com.example.application.views.products.event;

import com.example.application.data.entity.Products;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

@Getter
public class ProductEvent extends ComponentEvent<Component> {
    private final Products item;

    public ProductEvent(Component source, boolean fromClient, Products item) {
        super(source, fromClient);
        this.item = item;
    }
}