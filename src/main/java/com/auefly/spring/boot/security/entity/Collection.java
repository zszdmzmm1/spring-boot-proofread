package com.auefly.spring.boot.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Collection {
    @Id
    private Long id;

    private String title;
    private String titleTranslation;
    private String slug;
    private String type;
    private String video;
    private long duration;
    private String cover;
    private String description;
    private boolean published;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    //doesn't need it for now
    private String content;
    private boolean free;
    private Double price;
    private String seoTitle;
    private String seoDescription;
    private boolean completed;
    private boolean proofread;
}
