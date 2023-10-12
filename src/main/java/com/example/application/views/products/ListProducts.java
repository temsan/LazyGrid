package com.example.application.views.products;

import com.example.application.data.entity.FileStorage;
import com.example.application.data.entity.Products;
import com.example.application.data.service.ProductsRepository;
import com.example.application.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.PageRequest;
import org.vaadin.firitin.components.grid.PagingGrid;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AnonymousAllowed
@PageTitle("Products")
@Route(value = "products", layout = MainLayout.class)
public class ListProducts extends VerticalLayout {

    public ListProducts(ProductsRepository service) {
        setSizeFull();

        var grid = new PagingGrid<>(Products.class);
        grid.setColumns("name", "number");

        // Настройка DataProvider
        grid.setPagingDataProvider((page, pageSize) -> {
            return service.findAll(PageRequest.of((int) page, pageSize)).stream().toList();
        });

        grid.addColumn(new ComponentRenderer<>(product -> {
            if (!product.getPreview().isEmpty()) {
                return createImage(product.getPreview());
            } else {
                return createPlaceholderImage();
            }
        })).setHeader("Preview Image").setKey("previewImage");

        grid.addItemClickListener(item -> {
            Products model = item.getItem();
            Dialog dialog = new Dialog();
            dialog.setWidth("800px");
            dialog.setHeight("600px");
            PageProducts pageProducts = new PageProducts(dialog, model, service, grid);
            dialog.add(pageProducts);
            dialog.setCloseOnEsc(true);
            dialog.open();
        });

        var add = new Button("ADD");
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickListener(event -> {
            Products model = new Products();
            Dialog dialog = new Dialog();
            dialog.setWidth("800px");
            dialog.setHeight("400px");
            dialog.add(new PageProducts(dialog, model, service, grid));
            dialog.setCloseOnEsc(true);
            dialog.open();
        });

        H3 title = new H3("PRODUCTS");
        add(title, add, grid);
    }

    private Image createImage(FileStorage firstImage) {
        byte[] imageData = firstImage.getData();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
            BufferedImage originalImage = ImageIO.read(inputStream);
            BufferedImage thumbnail = Thumbnails.of(originalImage)
                    .size(80, 80)
                    .asBufferedImage();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, getFileExtension(firstImage.getName()), outputStream);
            byte[] previewImageData = outputStream.toByteArray();
            return getImage(firstImage, previewImageData);
        } catch (IOException e) {
            e.printStackTrace();
            return createPlaceholderImage();
        }
    }

    private static Image getImage(FileStorage firstImage, byte[] previewImageData) {
        String imageApiBaseUrl = "/api/pictures/";
        String imageUrl = imageApiBaseUrl + firstImage.getProduct().getId(); // Используем productId в качестве части URL
        Image imagePreview = new Image(new StreamResource("preview.jpg", () -> new ByteArrayInputStream(previewImageData)), "Preview");
        imagePreview.setWidth("80px");
        imagePreview.setHeight("80px");
        imagePreview.setSrc(imageUrl + " 1x, " + imageUrl + " 2x"); // Указываем пути к изображениям разного разрешения
        return imagePreview;
    }

    private Image createPlaceholderImage() {
        Image placeholderImage = new Image("images/placeholder.png", "No Image");
        placeholderImage.setWidth("80px");
        placeholderImage.setHeight("80px");
        return placeholderImage;
    }

    private static String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}