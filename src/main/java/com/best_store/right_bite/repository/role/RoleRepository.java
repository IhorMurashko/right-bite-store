package com.best_store.right_bite.repository.role;

import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional <Role> findByName(@NonNull RoleName name);
}
