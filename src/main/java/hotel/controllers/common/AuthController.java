package hotel.controllers.common;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hotel.dto.user.JwtRequest;
import hotel.dto.user.JwtResponse;
import hotel.dto.user.RefreshTokenRequest;
import hotel.dto.user.RegisterRequest;
import hotel.entities.security.User;
import hotel.services.security.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/sign-in")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody JwtRequest jwtRequest) {
		JwtResponse response = authService.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.ok(authService.register(registerRequest));
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		return ResponseEntity.ok(authService.refreshAccessToken(request.getRefreshToken()));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
		authService.logout(request.getRefreshToken());
		return ResponseEntity.ok().build();
	}
}
