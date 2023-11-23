package com.auefly.spring.boot.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String slug;
    String title;
    String titleTranslation;
    String description;
    int sortOrder;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @ManyToOne(
            targetEntity = Collection.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "collection_id", referencedColumnName = "id")
    Collection collection;
}
