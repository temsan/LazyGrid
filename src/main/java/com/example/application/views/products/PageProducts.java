package com.example.application.views.products;

import com.example.application.data.entity.FileStorage;
import com.example.application.data.entity.Products;
import com.example.application.data.service.ProductsRepository;
import com.example.application.util.Utils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
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
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@AnonymousAllowed
@PageTitle("PageProducts")
public class PageProducts extends VerticalLayout {

    private final Binder<Products> binder = new Binder<>(Products.class);
    private final Products model;
    private final ProductsRepository service;
    private final Dialog dialog;
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload imageUpload = new Upload(buffer);

    public PageProducts(Dialog dialog, Products model, ProductsRepository service, Grid<Products> grid) {
        this.model = model;
        this.service = service;
        this.dialog = dialog;

        H3 title = new H3(model.getName());

        TextField name = new TextField("Name");
        name.setWidth("100%");

        TextField number = new TextField("Number");
        number.setWidth("100%");

        Image imagePreview = getPreview(model);

        imageUpload.setAcceptedFileTypes("image/*");
        imageUpload.setMaxFiles(1);
        imageUpload.setDropAllowed(false);

        var update = new Button("UPDATE");
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        update.addClickListener(event -> {
            try {
                binder.writeBean(model);
                service.save(model);
                dialog.close();
                grid.getDataProvider().refreshAll(); // Обновляем сетку после закрытия диалога

            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(null, "An error occurred. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        binder.forField(name).bind(Products::getName, Products::setName);

        binder.forField(number).withConverter(new StringToIntegerConverter("Please enter a valid number")).bind(Products::getNumber, Products::setNumber);

        imageUpload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream fileStream = buffer.getInputStream(); // buffer - ваш MemoryBuffer

            imagePreview.setSrc(fileName);
            // Обработка загруженного файла
            uploadImage(fileName, fileStream);
        });


        binder.readBean(model);
        add(title, name, number, imageUpload, imagePreview, update);
        // Добавляем обработку перетаскивания файлов
        setupDragAndDrop();
    }

    static Image getPreview(Products model) {
        Image imagePreview = new Image();

        FileStorage preview = model.getPreview();
        if (preview != null && !preview.isEmpty()) {
            byte[] imageData = preview.getData();
            String fileName = preview.getName() != null ? preview.getName() : "default-image-name"; // Здесь используется имя по умолчанию, если имя файла равно null
            StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(imageData));
            imagePreview.setSrc(resource);
        }

        imagePreview.setWidth("100px");
        imagePreview.setHeight("100px");

        return imagePreview;
    }

    private void setupDragAndDrop() {
        imageUpload.setAcceptedFileTypes("image/*");
        imageUpload.setDropAllowed(true);

        imageUpload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream fileStream = buffer.getInputStream(); // buffer - ваш MemoryBuffer

            // Обработка загруженного файла
            uploadImage(fileName, fileStream);
        });
    }

    private void uploadImage(String fileName, InputStream fileStream) {
        try {
            byte[] imageData = IOUtils.toByteArray(fileStream);

            // Создание и сохранение превью изображения
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
            BufferedImage thumbnail = Scalr.resize(originalImage, 100); // Размер превью (100x100)
            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, Utils.getFileExtension(fileName), thumbnailStream); // Используйте "jpg" или другой формат
            model.setImagePreview(thumbnailStream.toByteArray(), fileName);
            model.setImage(imageData); // Сохранение основного изображения

            // service.saveImage(imageData);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
