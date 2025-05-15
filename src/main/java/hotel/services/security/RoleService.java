package hotel.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.security.Role;
import hotel.entities.security.RoleType;
import hotel.exceptions.NotFoundException;
import hotel.repository.interfaces.RoleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;

	public Role getRoleByRoleType(RoleType roleType) {
		return roleRepository.findByRoleType(roleType).orElseThrow(() -> new NotFoundException("Default role not found"));
	}
}
