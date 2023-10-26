package com.example.application.controller;

import com.example.application.data.entity.FileStorage;
import com.example.application.data.entity.Products;
import com.example.application.data.service.ProductsRepository;
import com.example.application.util.Utils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
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
    public ResponseEntity<InputStreamResource> getPicture(@PathVariable UUID productId) {
        Optional<Products> productOptional = productsRepository.findById(productId);

        if (productOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            FileStorage preview = productOptional.get().getPreview();

            if (!preview.isEmpty()) {
                byte[] imageData = preview.getData();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

                String fileExtension = Utils.getFileExtension(preview.getName());
                MediaType mediaType = getMediaTypeFromExtension(fileExtension);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(mediaType);
                headers.setContentDispositionFormData("attachment", preview.getName());

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .headers(headers)
                        .body(new InputStreamResource(inputStream));
            } else {
                // Если у продукта нет изображения, возвращаем HTTP-код 404 Not Found
                return ResponseEntity.notFound().build();
            }
        }
    }

    private MediaType getMediaTypeFromExtension(String fileExtension) {
        return switch (fileExtension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}