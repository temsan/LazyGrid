package com.example.application.data.entity;

import com.example.application.util.Utils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Products {
    @Id
    @Column(name = "id", columnDefinition = "uuid default uuid_generate_v4()")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    @Setter
    @Column(name = "name", length = 250, nullable = true, unique = false)
    private String name;

    @Getter
    @Setter
    private Integer number;

    @Getter
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FileStorage> images = new ArrayList<>();

    public void setImage(byte[] imageData) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setData(imageData);
        fileStorage.setProduct(this); // Устанавливаем обратную ссылку
        this.images.add(fileStorage);
    }

    public void setImagePreview(byte[] byteArray, String fileName) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setName(fileName);
        fileStorage.setMime_type(Utils.getFileExtension(fileName));
        fileStorage.setData(byteArray);
        fileStorage.setProduct(this); // Устанавливаем обратную ссылку
        if (images.isEmpty()) {
            images.add(fileStorage);
        } else {
            this.images.set(0, fileStorage);
        }
    }

    public FileStorage getPreview() {
        return images.stream().findFirst().orElse(new FileStorage());
    }
}
