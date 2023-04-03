package com.bankofbaku.SpringSecurityJWT.repositories;

import com.bankofbaku.SpringSecurityJWT.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
