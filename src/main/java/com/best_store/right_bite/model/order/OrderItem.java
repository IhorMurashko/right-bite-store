package com.best_store.right_bite.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_items_seq_generator")
    @SequenceGenerator(name = "order_items_seq_generator", sequenceName = "order_item_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, length = 50)
    private String productName;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal priceSnapshot;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public BigDecimal getTotalPrice() {
        return priceSnapshot.multiply(BigDecimal.valueOf(quantity));
    }

   public void setOrder(@NotNull Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
