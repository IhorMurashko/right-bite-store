package com.best_store.right_bite.model.cart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single line item within a {@link Cart}.
 * <p>
 * Each cart item corresponds to a specific product and holds information
 * like quantity and price. Price and product details are stored as snapshots
 * to ensure the cart's historical accuracy even if the original product data changes.
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    /**
     * Unique identifier for the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_generator")
    @SequenceGenerator(name = "item_seq_generator", sequenceName = "item_seq", allocationSize = 1)
    private Long id;

    /**
     * The ID of the product this cart item represents.
     * Stored directly to avoid a hard foreign key relationship to a Product entity.
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * A snapshot of the product's name at the time of adding to the cart.
     */
    @Column(nullable = false)
    private String productName;

    /**
     * The number of units of the product in the cart.
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * A snapshot of the price per unit at the time the item was added to the cart.
     * This preserves the price for the order, regardless of future product price changes.
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPriceSnapshot;

    /**
     * The timestamp when the price snapshot was taken.
     */
    @Column(nullable = false)
    private LocalDateTime priceSnapshotTime;

    /**
     * The total price for this line item, calculated as (quantity * unitPriceSnapshot).
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    /**
     * A snapshot of the URL for the product's thumbnail image.
     */
    @Column(nullable = false)
    private String thumbnailUrl;

    /**
     * The parent {@link Cart} to which this item belongs.
     * Fetched lazily for performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Constructs a new CartItem with all required details.
     *
     * @param productId         the ID of the product.
     * @param productName       the name of the product.
     * @param quantity          the quantity of the product.
     * @param unitPriceSnapshot the price per unit.
     * @param totalPrice        the total price for the line item.
     * @param thumbnailUrl      the URL of the product's thumbnail.
     * @param priceSnapshotTime the timestamp of the price capture.
     * @param cart              the parent cart.
     */
    public CartItem(Long productId, String productName, int quantity,
                    BigDecimal unitPriceSnapshot, BigDecimal totalPrice,
                    String thumbnailUrl, LocalDateTime priceSnapshotTime, Cart cart) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPriceSnapshot = unitPriceSnapshot;
        this.totalPrice = totalPrice;
        this.thumbnailUrl = thumbnailUrl;
        this.priceSnapshotTime = priceSnapshotTime;
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
