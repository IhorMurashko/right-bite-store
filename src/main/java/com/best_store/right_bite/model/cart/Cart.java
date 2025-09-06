package com.best_store.right_bite.model.cart;

import com.best_store.right_bite.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents a user's shopping cart as a persistent entity.
 * <p>
 * A cart is uniquely associated with a {@link User} and contains a collection of
 * {@link CartItem}s. The lifecycle of {@code CartItem}s is directly managed
 * by the cart through cascading operations and orphan removal.
 */
@Entity
@Table(name = "carts")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Cart {

    /**
     * Constructs a new Cart for the given User.
     *
     * @param user the user who owns this cart. Must not be null.
     */
    public Cart(User user) {
        this.user = user;
    }

    /**
     * Unique identifier for the cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_seq_generator")
    @SequenceGenerator(name = "cart_seq_generator", sequenceName = "cart_seq", allocationSize = 1)
    private Long id;

    /**
     * The user who owns this shopping cart.
     * Fetched lazily to optimize performance.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * A set of items currently in the cart.
     * <p>
     * This collection is eagerly fetched and ordered by ID. Changes to this set
     * are cascaded to the {@link CartItem} entity, and removing an item from this
     * collection will also delete it from the database due to {@code orphanRemoval=true}.
     */
    @OrderBy("id ASC")
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    /**
     * The timestamp when the cart was created.
     * This is automatically set by JPA auditing.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp of the last update to the cart.
     * This is automatically managed by JPA auditing.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * The calculated total price of all items in the cart.
     */
    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * Returns an unmodifiable view of the items in the cart.
     * This prevents direct modification of the underlying collection from outside the class.
     *
     * @return an unmodifiable {@link Set} of {@link CartItem}s.
     */
    public Set<CartItem> getCartItems() {
        return Collections.unmodifiableSet(cartItems);
    }

    /**
     * Adds a new item to the cart and establishes the bidirectional relationship.
     *
     * @param item the {@link CartItem} to add.
     */
    public void addItem(CartItem item) {
        item.setCart(this);
        this.cartItems.add(item);
    }

    /**
     * Removes items from the cart that match the given predicate.
     *
     * @param filter a predicate to apply to each cart item to determine if it should be removed.
     */
    public void removeItemsByProductIds(Predicate<CartItem> filter) {
        cartItems.removeIf(filter);
    }

    /**
     * Removes all items from the cart.
     */
    public void clear() {
        this.cartItems.clear();
        this.totalPrice = BigDecimal.ZERO;
    }

    /**
     * Compares this cart to another object for equality.
     * Two carts are considered equal if they have the same non-null ID.
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        if (this.id == null || cart.id == null) return false;
        return Objects.equals(id, cart.id);
    }

    /**
     * Generates a hash code for the cart based on its ID.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
