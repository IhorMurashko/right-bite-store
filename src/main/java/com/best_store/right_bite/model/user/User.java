package com.best_store.right_bite.model.user;


import com.best_store.right_bite.model.auth.AuthProvider;
import com.best_store.right_bite.model.role.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * JPA entity representing an application user with extended profile details.
 *
 * <p>Extends {@link AbstractUser} to include personal and address information.</p>
 *
 * <p>Stored in the {@code users} table. Also maintains a one-to-many relationship
 * with {@link BillingInfo} for billing records.</p>
 *
 * <p>Additional fields include:</p>
 * <ul>
 *     <li><b>firstName</b>, <b>lastName</b> — optional full name</li>
 *     <li><b>imageUrl</b> — optional avatar image URL</li>
 *     <li><b>phoneNumber</b> — optional contact number</li>
 *     <li><b>country</b>, <b>city</b>, <b>streetName</b>, <b>houseNumber</b>, <b>zipCode</b> — address fields</li>
 * </ul>
 *
 * @author Ihor Murashko
 */

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractUser {

    private String firstName;
    private String lastName;
    private String imageUrl;
    private String phoneNumber;
    private String country;
    private String city;
    private String streetName;
    private String houseNumber;
    private String zipCode;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingInfo> billingInfos = new ArrayList<>();

    public User(String email, String password,
                boolean isAccountNonExpired, boolean isAccountNonLocked,
                boolean isCredentialsNonExpired, boolean isEnabled,
                Set<Role> roles, AuthProvider authProvider, String oauthId) {
        super(email, password, isAccountNonExpired, isAccountNonLocked,
                isCredentialsNonExpired, isEnabled, roles, authProvider, oauthId);
    }
}
