//package com.example.application.views.products.event;
//
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import com.vaadin.flow.data.provider.ListDataProvider;
//import com.example.application.data.entity.Products;
//
//@Component
//public class ProductEventListener {
//    private final ListDataProvider<Products> dataProvider;
//
//    public ProductEventListener(ListDataProvider<Products> dataProvider) {
//        this.dataProvider = dataProvider;
//    }
//
//    @EventListener
//    public void handleProductEvent(ProductEvent event) {
//        // Получите объект Products из события
//        Products product = event.getProduct();
//
//        // Обновите данные в гриде
//        dataProvider.refreshItem(product);
//    }
//}
