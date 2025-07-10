package com.best_store.right_bite.model.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
public class Category extends Base {

    @Column(name = "category_name")
    String categoryName;

    @ManyToMany(mappedBy = "categories")
    List<Product> product;

    String image;
}
