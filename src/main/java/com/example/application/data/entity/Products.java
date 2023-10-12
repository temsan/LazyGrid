package com.example.application.data.entity;

import com.example.application.util.Utils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Products {
    @Id
    @Column(name = "id", columnDefinition = "uuid default uuid_generate_v4()")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 250, nullable = true, unique = false)
    private String name;

    private Integer number;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FileStorage> images = new LinkedList<>();

    public void setImage(byte[] imageData) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setData(imageData);
        fileStorage.setProduct(this);
        this.images.add(fileStorage);
    }

    public void setImagePreview(byte[] byteArray, String fileName) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setName(fileName);
        fileStorage.setMime_type(Utils.getFileExtension(fileName));
        fileStorage.setData(byteArray);
        fileStorage.setProduct(this);
        if (images.isEmpty()) {
            images.add(fileStorage);
        } else {
            this.images.set(0, fileStorage);
        }
    }

    public FileStorage getPreview() {
        return images.isEmpty() ? new FileStorage() : images.get(0);
    }
}