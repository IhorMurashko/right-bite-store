package com.best_store.right_bite.model.catalog;

import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.utils.utilsBMI.BMICategoryPostgresConverter;
import jakarta.persistence.*;
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
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private List<Product> product;

    @Convert(converter = BMICategoryPostgresConverter.class)
    @Column(name = "index_body")
    private BMICategory indexBody;

    private String image;
}
