package com.kairgaliyev.backendonlineshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "category")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "parent_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity parentCategory;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductEntity> products = new ArrayList<>();
}

