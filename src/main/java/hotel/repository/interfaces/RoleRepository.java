package hotel.repository.interfaces;

import hotel.entities.security.Role;
import hotel.entities.security.RoleType;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
	Optional<Role> findByRoleType(RoleType roleType);
	Role save(Role role);
	Optional<Role> findById(Long id);
	List<Role> findAll();
}
