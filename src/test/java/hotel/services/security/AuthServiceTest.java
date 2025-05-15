package senla.education.hotel.services.security;

import hotel.services.security.AuthService;
import hotel.services.security.JwtTokenService;
import hotel.services.security.RefreshTokenService;
import hotel.services.security.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import hotel.dto.user.JwtResponse;
import hotel.dto.user.RegisterRequest;
import hotel.entities.security.RefreshToken;
import hotel.entities.security.Role;
import hotel.entities.security.RoleType;
import hotel.entities.security.User;
import hotel.exceptions.JwtExpiredException;
import hotel.exceptions.UsernameAlreadyExistsException;
import hotel.mappers.UserMapper;
import hotel.repository.interfaces.UserRepository;
import senla.education.hotel.util.TestObjectUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private RefreshTokenService refreshTokenService;
	@Mock
	private JwtTokenService jwtTokenService;
	@Mock
	private RoleService roleService;
	@Mock
	private UserMapper userMapper;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@InjectMocks
	private AuthService authService;

	private User user;
	private RefreshToken refreshToken;

	@BeforeEach
	void setUp() {
		user = TestObjectUtils.createTestUser();
		refreshToken = TestObjectUtils.createTestRefreshToken(user);
	}

	@Test
	void shouldAuthenticateTest() {
		// Given
		String username = "testUser";
		String password = "testPass";
		String accessToken = "accessToken";
		String refreshTokenValue = "refreshToken";

		UserDetails userDetails = TestObjectUtils.createTestUserDetails();
		Authentication authentication = mock(Authentication.class);

		// When
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(refreshTokenService.generateRefreshToken(user)).thenReturn(refreshToken);
		when(jwtTokenService.generateAccessToken(userDetails)).thenReturn(accessToken);

		JwtResponse actual = authService.authenticate(username, password);

		// Then
		assertNotNull(actual);
		assertEquals(accessToken, actual.getAccessToken());
		assertEquals(refreshTokenValue, actual.getRefreshToken());
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository, times(1)).findByUsername(username);
		verify(refreshTokenService, times(1)).generateRefreshToken(user);
		verify(jwtTokenService, times(1)).generateAccessToken(userDetails);
	}

	@Test
	void shouldRefreshAccessTokenTest() {
		// Given
		String refreshTokenValue = "refreshToken";
		String newAccessToken = "newAccessToken";

		// When
		when(refreshTokenService.getRefreshTokenByValue(refreshTokenValue)).thenReturn(refreshToken);
		when(jwtTokenService.generateAccessToken(any(UserDetails.class))).thenReturn(newAccessToken);

		JwtResponse actual = authService.refreshAccessToken(refreshTokenValue);

		// Then
		assertNotNull(actual);
		assertEquals(newAccessToken, actual.getAccessToken());
		assertEquals(refreshTokenValue, actual.getRefreshToken());
		verify(refreshTokenService, times(1)).getRefreshTokenByValue(refreshTokenValue);
		verify(jwtTokenService, times(1)).generateAccessToken(any(UserDetails.class));
	}

	@Test
	void shouldNotRefreshAccessTokenWhenRefreshTokenIsExpiredTest() {
		// Given
		String refreshTokenValue = "expiredToken";
		RefreshToken expiredRefreshToken = TestObjectUtils.createTestRefreshToken(TestObjectUtils.createTestUser());
		expiredRefreshToken.setToken(refreshTokenValue);
		expiredRefreshToken.setExpiryDate(Instant.now().minusSeconds(1000));

		// When
		when(refreshTokenService.getRefreshTokenByValue(refreshTokenValue)).thenReturn(expiredRefreshToken);
		doNothing().when(refreshTokenService).deleteRefreshToken(expiredRefreshToken);

		// Then
		assertThrows(JwtExpiredException.class, () -> authService.refreshAccessToken(refreshTokenValue));
		verify(refreshTokenService, times(1)).getRefreshTokenByValue(refreshTokenValue);
		verify(refreshTokenService, times(1)).deleteRefreshToken(expiredRefreshToken);
	}

	@Test
	void shouldRegisterTest() {
		// Given
		RegisterRequest registerRequest = TestObjectUtils.createTestRegisterRequest();
		Role role = TestObjectUtils.createTestRole();

		// When
		when(userMapper.toEntity(registerRequest)).thenReturn(user);
		when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
		when(roleService.getRoleByRoleType(RoleType.ROLE_USER)).thenReturn(role);
		when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(user);

		User actual = authService.register(registerRequest);

		// Then
		assertNotNull(actual);
		assertEquals(role, actual.getRole());
		verify(userMapper, times(1)).toEntity(registerRequest);
		verify(userRepository, times(1)).existsByUsername(user.getUsername());
		verify(roleService, times(1)).getRoleByRoleType(RoleType.ROLE_USER);
		verify(userRepository, times(1)).save(user);
	}

	@Test
	void shouldNotRegisterWhenUsernameAlreadyExistsTest() {
		// Given
		RegisterRequest registerRequest = TestObjectUtils.createTestRegisterRequest();

		// When
		when(userMapper.toEntity(registerRequest)).thenReturn(user);
		when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

		// Then
		assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(registerRequest));
		verify(userMapper, times(1)).toEntity(registerRequest);
		verify(userRepository, times(1)).existsByUsername(user.getUsername());
		verifyNoInteractions(roleService, passwordEncoder);
		verify(userRepository, never()).save(any());
	}

	@Test
	void shouldLogoutTest() {
		// Given
		String refreshTokenValue = "refreshToken";

		// When
		when(refreshTokenService.getRefreshTokenByValue(refreshTokenValue)).thenReturn(refreshToken);
		doNothing().when(refreshTokenService).deleteRefreshToken(refreshToken);

		authService.logout(refreshTokenValue);

		// Then
		verify(refreshTokenService, times(1)).getRefreshTokenByValue(refreshTokenValue);
		verify(refreshTokenService, times(1)).deleteRefreshToken(refreshToken);
	}
}