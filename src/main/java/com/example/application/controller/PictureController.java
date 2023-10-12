package com.example.application.controller;

import com.example.application.data.entity.FileStorage;
import com.example.application.data.entity.Products;
import com.example.application.data.service.ProductsRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    private final ProductsRepository productsRepository;

    public PictureController(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InputStreamResource> getPicture(@PathVariable UUID productId) throws IOException {
        Optional<Products> productOptional = productsRepository.findById(productId);

        if (productOptional.isPresent()) {
            Products product = productOptional.get();
            FileStorage preview = product.getPreview();

            if (!preview.isEmpty()) {
                byte[] imageData = preview.getData();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

                String fileExtension = getFileExtension(preview.getName());
                MediaType mediaType = getMediaTypeFromExtension(fileExtension);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(new InputStreamResource(inputStream));
            }
        }

        return ResponseEntity.notFound().build();
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    private MediaType getMediaTypeFromExtension(String fileExtension) {
        return switch (fileExtension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}