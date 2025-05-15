package hotel.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.dto.user.JwtResponse;
import hotel.dto.user.RegisterRequest;
import hotel.entities.security.RefreshToken;
import hotel.entities.security.RoleType;
import hotel.entities.security.User;
import hotel.exceptions.JwtExpiredException;
import hotel.exceptions.UsernameAlreadyExistsException;
import hotel.mappers.UserMapper;
import hotel.repository.interfaces.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;
	private final JwtTokenService jwtTokenService;
	private final RoleService roleService;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public JwtResponse authenticate(String username, String password) {
		log.info("Attempting authentication for user: {}", username);
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);
		String accessToken = jwtTokenService.generateAccessToken(userDetails);

		log.info("Authentication successful for user: {}", username);
		return JwtResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken.getToken())
				.build();
	}

	@Transactional
	public JwtResponse refreshAccessToken(String refreshTokenValue) {
		RefreshToken refreshToken = refreshTokenService.getRefreshTokenByValue(refreshTokenValue);
		if (refreshToken.isExpired()) {
			refreshTokenService.deleteRefreshToken(refreshToken);
			throw new JwtExpiredException("Refresh token is expired or invalid");
		}

		User user = refreshToken.getUser();
		UserDetails userDetails = CustomUserDetails.builder().user(user).build();
		String newAccessToken = jwtTokenService.generateAccessToken(userDetails);
		return JwtResponse.builder()
				.accessToken(newAccessToken)
				.refreshToken(refreshTokenValue)
				.build();
	}

	@Transactional
	public User register(RegisterRequest registerRequest) {
		User user = userMapper.toEntity(registerRequest);
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new UsernameAlreadyExistsException("Username is already taken");
		}
		user.setRole(roleService.getRoleByRoleType(RoleType.ROLE_USER));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Transactional
	public void logout(String refreshTokenValue) {
		RefreshToken refreshToken = refreshTokenService.getRefreshTokenByValue(refreshTokenValue);
		refreshTokenService.deleteRefreshToken(refreshToken);
	}
}
