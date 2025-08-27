package com.best_store.right_bite.model.order;

import com.best_store.right_bite.constant.order.DeliveryMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_delivery_details")
@Getter
@Setter
@NoArgsConstructor
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
    @Column(nullable = false, length = 50)
    private String houseNumber;
    @Column(nullable = false, length = 100)
    private String streetName;
    @Column(nullable = false, length = 50)
    private String city;
    @Column(nullable = false, length = 50)
    private String country;
    @Column(nullable = false, length = 15)
    private String zipCode;
    @Column(nullable = true, length = 150)
    private String comment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryMethod deliveryMethod;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
