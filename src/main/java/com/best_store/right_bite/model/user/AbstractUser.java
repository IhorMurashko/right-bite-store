package com.best_store.right_bite.model.user;


import com.best_store.right_bite.model.auth.AuthProvider;
import com.best_store.right_bite.model.role.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract base class for user entities in the system.
 *
 * <p>This class defines the common structure for all user-related entities, such as customers or admins.
 * It includes authentication and authorization fields, account status flags, audit timestamps,
 * and role mapping via an ElementCollection.</p>
 *
 * <p>Designed as a MappedSuperclass for JPA, it should be extended by actual entity classes.</p>
 * <p>
 * Fields:
 * - id: unique user identifier (auto-generated)
 * - email, password: required login credentials
 * - roles: collection of user roles (as enum values)
 * - account status flags: track account state (locked, expired, etc.)
 * - createdAt, updatedAt: audit timestamps
 * <p>
 * Equality is based on user ID.
 *
 * @author Ihor Murashko
 */


@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractUser {

    public AbstractUser(String email, String password,
                        boolean isAccountNonExpired, boolean isAccountNonLocked,
                        boolean isCredentialsNonExpired, boolean isEnabled,
                        Set<Role> roles,
                        AuthProvider authProvider, String oauthId) {
        this.email = email;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
        this.authProvider = authProvider;
        this.oauthId = oauthId;
    }

    /**
     * Unique identifier for the user. Auto-generated using a sequence strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    protected Long id;

    /**
     * User email address. Must be unique and non-null. Used as a primary login credential.
     */
    @Column(unique = true, nullable = false)
    protected String email;

    /**
     * Encrypted user password.
     * Can be nullable for users registered with social network authentication.
     */
    @Column(nullable = true)
    protected String password;

    /**
     * Authentication provider for the user.
     * For example: LOCAL, GOOGLE, FACEBOOK, etc.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected AuthProvider authProvider;

    /**
     * Unique identifier from the OAuth provider.
     * Nullable for users registered with local authentication.
     */
    @Column(nullable = true)
    protected String oauthId;

    /**
     * Flag indicating whether the user's account is expired.
     */
    @Column(nullable = false, columnDefinition = "boolean default true")
    protected boolean isAccountNonExpired;

    /**
     * Flag indicating whether the user's account is locked.
     */
    @Column(nullable = false, columnDefinition = "boolean default true")
    protected boolean isAccountNonLocked;

    /**
     * Flag indicating whether the user's credentials (password) are expired.
     */
    @Column(nullable = false, columnDefinition = "boolean default true")
    protected boolean isCredentialsNonExpired;

    /**
     * Flag indicating whether the user's account is enabled.
     */
    @Column(nullable = false, columnDefinition = "boolean default true")
    protected boolean isEnabled;

    /**
     * Set of roles (authorities) granted to the user.
     * Stored as a collection of enum values using a separate table 'user_roles'.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    protected Set<Role> roles = new HashSet<>();

    /**
     * Timestamp when the user was created. Automatically set by JPA auditing.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /**
     * Timestamp of the last update to the user's record. Automatically managed.
     */
    @LastModifiedDate
    protected LocalDateTime updatedAt;

    /**
     * Checks equality based on class type and unique user ID.
     *
     * @param o the object to compare
     * @return true if the objects are of the same type and have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractUser that = (AbstractUser) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Computes hash code based on the user's ID.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

