package com.example.application.views.products;

import com.example.application.data.entity.Products;
import com.example.application.data.service.ProductsRepository;
import com.example.application.layouts.MainLayout;
import com.example.application.views.products.event.DataUpdateListener;
import com.example.application.views.products.event.DataUpdateManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AnonymousAllowed
@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
public class ListProducts extends VerticalLayout implements DataUpdateListener {

    private final Grid<Products> grid;

    public void refreshAllGrids(Products item) {
        DataUpdateManager.notifyListeners();
    }

    @Override
    public void dataUpdated() {
        grid.getDataProvider().refreshAll();
    }

    public void updateItem(Products item) {
        grid.getDataProvider().refreshItem(item);
    }

    public ListProducts(ProductsRepository service) {
        setSizeFull();

        grid = getProductsGrid(service);

        grid.addColumn(new ComponentRenderer<>(product -> {
            if (!product.getPreview().isEmpty()) {
                return getImage(product.getId());
            } else {
                return null;
            }
        })).setHeader("Preview Image").setKey("previewImage");

        grid.addItemClickListener(item -> {
            openDialog(service, item.getItem(), this);
        });

        var add = new Button("ADD");
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickListener(event -> {
            openDialog(service, new Products(), this);
        });

        H3 title = new H3("PRODUCTS");
        add(title, add, grid);

        DataUpdateManager.addListener(this); // Регистрируем этот экземпляр как подписчика
    }

    private static void openDialog(ProductsRepository service, Products model, ListProducts list) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeight("600px");
        dialog.add(new PageProducts(dialog, model, service, list));
        dialog.setCloseOnEsc(true);
        dialog.open();
    }

    public static Grid<Products> getProductsGrid(ProductsRepository service) {
        var grid = new Grid<>(Products.class);
        grid.setColumns("id", "name", "number");

        // Setting up DataProvider with sorting on multiple fields
        grid.setDataProvider(DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    // Обработка мультисортировки
                    List<QuerySortOrder> sortOrders = query.getSortOrders();

                    List<Sort.Order> orders = new ArrayList<>();
                    for (QuerySortOrder sortOrder : sortOrders) {
                        if (sortOrder.getDirection() != null) {
                            Sort.Direction direction = sortOrder.getDirection() == SortDirection.ASCENDING ?
                                    Sort.Direction.ASC : Sort.Direction.DESC;
                            orders.add(Sort.Order.by(sortOrder.getSorted()).with(direction));
                        }
                    }

                    Sort sort = Sort.by(orders);

                    // Сортировка по-умолчанию
                    sort = Sort.by("name");

                    return service.findAll(PageRequest.of(offset, limit, sort)).stream();
                },
                query -> Math.toIntExact(service.count())
        ));

        grid.setSortableColumns("id", "name", "number");
        grid.setMultiSort(true, true);
        return grid;
    }


    public static Image getImage(UUID productId) {
        String imageApiBaseUrl = "/api/pictures/";
        String imageUrl = imageApiBaseUrl + productId;

        Image imagePreview = new Image(imageUrl, "Thumbnail");
        imagePreview.setWidth("80px");
        imagePreview.setHeight("80px");

        return imagePreview;
    }
}