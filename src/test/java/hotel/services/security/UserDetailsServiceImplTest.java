package senla.education.hotel.services.security;

import hotel.services.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import hotel.entities.security.User;
import hotel.repository.interfaces.UserRepository;
import senla.education.hotel.util.TestObjectUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	@Test
	void shouldLoadUserByUsernameTest() {
		// Given
		String username = "testUser";
		User user = TestObjectUtils.createTestUser();

		// When
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		UserDetails actual = userDetailsService.loadUserByUsername(username);

		// Then
		assertNotNull(actual);
		assertEquals(username, actual.getUsername());
		verify(userRepository, times(1)).findByUsername(username);
	}

	@Test
	void shouldNotLoadUserByUsernameWhenUsernameNotExistsTest() {
		// Given
		String username = "nonExistingUser";

		// When
		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		// Then
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
		verify(userRepository, times(1)).findByUsername(username);
	}
}