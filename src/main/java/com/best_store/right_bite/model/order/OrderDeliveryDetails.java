package com.best_store.right_bite.model.order;

import com.best_store.right_bite.constant.order.DeliveryMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "order_delivery_details")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(access = AccessLevel.PUBLIC)
public class OrderDeliveryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_delivery_details_seq_generator")
    @SequenceGenerator(name = "orders_delivery_details_seq_generator", sequenceName = "orders_delivery_details_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false, length = 50)
    private String firstname;
    @Column(nullable = true, length = 50)
    private String lastname;
    @Column(nullable = false, length = 15)
    private String phoneNumber;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = true, length = 10)
    private String houseNumber;
    @Column(nullable = true, length = 100)
    private String streetName;
    @Column(nullable = true, length = 50)
    private String city;
    @Column(nullable = true, length = 50)
    private String country;
    @Column(nullable = true, length = 15)
    private String zipCode;
    @Column(nullable = true, length = 150)
    private String comment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryMethod deliveryMethod;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    public void setOrder(@NotNull Order order) {
        this.order = order;
    }

    public void setDeliveryMethod(@NotNull DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDeliveryDetails other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
