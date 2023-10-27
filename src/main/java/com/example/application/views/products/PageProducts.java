package com.example.application.views.products;

import com.example.application.data.entity.Products;
import com.example.application.data.service.ProductsRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@AnonymousAllowed
@PageTitle("PageProducts")
public class PageProducts extends VerticalLayout {
    private final Binder<Products> binder = new Binder<>(Products.class);
    private final Products product;
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload imageUpload = new Upload(buffer);
    private final Image imagePreview;

    public PageProducts(Dialog dialog, UUID productId, ProductsRepository repository, ListProducts listProducts) {
        product = repository.findById(productId).orElse(new Products());

        binder.setBean(product);
        H3 title = new H3(product.getName());

        TextField id = new TextField("ID");
        id.setWidth("100%");

        TextField name = new TextField("Name");
        name.setWidth("70%");

        TextField number = new TextField("Number");
        number.setWidth("30%");

        HorizontalLayout nameNumber = new HorizontalLayout(name, number);

        imagePreview = createImagePreview(product.getId());
        imageUpload.setAcceptedFileTypes("image/*");
        imageUpload.setMaxFiles(1);
        imageUpload.setDropAllowed(false);
        var update = new Button("UPDATE");
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickListener(event -> {
            try {
                binder.writeBean(product);
                repository.save(product);
                dialog.close();
                listProducts.refreshAllGrids(product);
            } catch (ValidationException e) {
                Dialog errorDialog = new Dialog();
                errorDialog.add(new Text("An error occurred. Please try again later."));
                errorDialog.open();
            }
        });

        binder.forField(id).bind(products -> products.getId().toString(), null);
        binder.forField(name).bind(Products::getName, Products::setName);
        binder.forField(number).withConverter(new StringToIntegerConverter("Please enter a valid number")).bind(Products::getNumber, Products::setNumber);
        imageUpload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream fileStream = buffer.getInputStream();
            uploadImage(fileName, fileStream);
        });
        binder.readBean(product);
        add(title, id, nameNumber, imageUpload, imagePreview, update);
        setupDragAndDrop();
    }

    private Image createImagePreview(UUID id) {
        Image imagePreview = new Image();
        imagePreview.setWidth("100px");
        imagePreview.setHeight("100px");
        String imageApiBaseUrl = "/api/pictures/";
        String imageUrl = imageApiBaseUrl + id;
        imagePreview.setSrc(imageUrl);
        return imagePreview;
    }

    private void setupDragAndDrop() {
        imageUpload.setAcceptedFileTypes("image/*");
        imageUpload.setDropAllowed(true);
        imageUpload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream fileStream = buffer.getInputStream();
            uploadImage(fileName, fileStream);
        });
    }

    private void uploadImage(String fileName, InputStream fileStream) {
        try {
            byte[] imageData = convertInputStreamToByteArray(fileStream);
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
            BufferedImage thumbnail = resizeImage(originalImage, 100, 100);
            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
            String fileExtension = getFileExtension(fileName);
            ImageIO.write(thumbnail, fileExtension, thumbnailStream);
            product.setImagePreview(thumbnailStream.toByteArray(), fileName);
            product.setImage(imageData);
            updateImagePreview(thumbnailStream.toByteArray());
        } catch (IOException e) {
            Dialog errorDialog = new Dialog();
            errorDialog.add(new Text("An error occurred. Please try again later."));
            errorDialog.open();
        }
    }

    private byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[inputStream.available()];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        return Scalr.resize(image, Scalr.Method.QUALITY, width, height);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    private void updateImagePreview(byte[] imageData) {
        StreamResource resource = new StreamResource("preview", () -> new ByteArrayInputStream(imageData));
        imagePreview.setSrc(resource);
    }
}