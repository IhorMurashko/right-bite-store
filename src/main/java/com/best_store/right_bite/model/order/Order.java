package com.best_store.right_bite.model.order;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "full_order",
        attributeNodes = {
                @NamedAttributeNode("items"),
                @NamedAttributeNode("orderDeliveryDetails")})
public class Order {

    @Version
    private Long version;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq_generator")
    @SequenceGenerator(name = "orders_seq_generator", sequenceName = "orders_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 20)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderDeliveryDetails orderDeliveryDetails;

    public Set<OrderItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void addItem(@NotNull OrderItem item) {
        if (this.items == null) {
            this.items = new LinkedHashSet<>();
        }
        items.add(item);
        item.setOrder(this);
        recalculateTotalPrice();
    }

    public void removeItem(@NotNull OrderItem item) {
        if (this.items == null) {
            return;
        }
        items.remove(item);
        item.setOrder(null);
        recalculateTotalPrice();
    }

    public void clear() {
        if (this.items == null) {
            return;
        }
        items.forEach(item -> item.setOrder(null));
        items.clear();
        totalPrice = BigDecimal.ZERO;
    }


    public void setOrderDeliveryDetails(@NotNull OrderDeliveryDetails orderDeliveryDetails) {
        this.orderDeliveryDetails = orderDeliveryDetails;
        orderDeliveryDetails.setOrder(this);
    }

    public void setOrderStatus(@NotNull OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    private void recalculateTotalPrice() {
        if (items == null) {
            return;
        }
        totalPrice = items.stream()
                .map(OrderItem::getPriceSnapshot)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        if (id == null || order.id == null) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}