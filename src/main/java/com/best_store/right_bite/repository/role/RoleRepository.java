package com.best_store.right_bite.repository.role;

import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
   @Query("SELECT r FROM Role r WHERE r.name = :name")
   Optional <Role> findByName(@NonNull @Param("name") RoleName name);
}
