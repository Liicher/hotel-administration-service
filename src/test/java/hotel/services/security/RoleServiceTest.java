package senla.education.hotel.services.security;

import hotel.services.security.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import hotel.entities.security.Role;
import hotel.entities.security.RoleType;
import hotel.exceptions.NotFoundException;
import hotel.repository.interfaces.RoleRepository;
import senla.education.hotel.util.TestObjectUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
	@Mock
	private RoleRepository roleRepository;
	@InjectMocks
	private RoleService roleService;

	@Test
	void shouldGetRoleByRoleTypeTest() {
		// Given
		RoleType roleType = RoleType.ROLE_USER;
		Role expectedRole = TestObjectUtils.createTestRole();

		// When
		when(roleRepository.findByRoleType(roleType)).thenReturn(Optional.of(expectedRole));

		Role actual = roleService.getRoleByRoleType(roleType);

		// Then
		assertNotNull(actual);
		assertEquals(roleType, actual.getRoleType());
		verify(roleRepository, times(1)).findByRoleType(roleType);
	}

	@Test
	void shouldNotGetRoleByRoleTypeWhenRoleNotExistsTest() {
		// Given
		RoleType roleType = RoleType.ROLE_USER;

		// When
		when(roleRepository.findByRoleType(roleType)).thenReturn(Optional.empty());

		// Then
		assertThrows(NotFoundException.class, () -> roleService.getRoleByRoleType(roleType));
		verify(roleRepository, times(1)).findByRoleType(roleType);
	}
}