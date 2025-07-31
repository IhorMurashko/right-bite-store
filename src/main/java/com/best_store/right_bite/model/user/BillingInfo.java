package com.best_store.right_bite.model.user;

import com.best_store.right_bite.utils.user.CardNumberEncryptor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "billing_info")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BillingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameOnCard;
    @Convert(converter = CardNumberEncryptor.class)
    private String cardNumber;
    private String expireDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
