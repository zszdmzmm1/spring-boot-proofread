package com.auefly.spring.boot.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DynamicUpdate
public class Collection {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String titleTranslation;
    @Column(unique = true)
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


    public Collection(Long id) {
        this.id = id;
    }


    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @OneToMany(mappedBy = "collection", fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "collection", fetch = FetchType.EAGER)
    private List<Lecture> lectures = new ArrayList<>();
}
