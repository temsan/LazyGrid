package com.example.application.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "files_storage")
public class FileStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    private byte[] data;

    private Integer size;
    private String name;

    private String mime_type;
    public String getMime_type() {
        return mime_type == null ? "jpg" : mime_type;
    }

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    public Boolean isEmpty() {
        return data == null || data.length == 0;
    }
}
