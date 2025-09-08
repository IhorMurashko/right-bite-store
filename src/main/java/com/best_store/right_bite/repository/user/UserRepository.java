package com.best_store.right_bite.repository.user;

import com.best_store.right_bite.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@NonNull String email);

    Optional<User> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);

    void deleteByEmail(@NonNull String email);

    boolean existsByEmail(@NonNull String email);

    boolean existsById(@NonNull Long id);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updateUserPassword(@Param("email") String email, @Param("password") String password);

    @Query("SELECT count(u.id) FROM User u ")
    Long countUsers();
}