package ma.sid.dao;

import ma.sid.dto.enums.RoleEnum;
import ma.sid.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByNomRole(RoleEnum role);
}