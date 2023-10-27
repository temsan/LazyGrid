package com.example.application.views.products.event;

import com.example.application.data.entity.Products;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductEvent extends ApplicationEvent {
    private final Products product;

    public ProductEvent(Object source, Products product) {
        super(source);
        this.product = product;
    }
}