package hotel.services.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.entities.security.RefreshToken;
import hotel.entities.security.User;
import hotel.repository.interfaces.RefreshTokenRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenDurationMs;
	@Value("${jwt.refresh.max}")
	private Long maxTokensPerUser;

	@Transactional
	public RefreshToken generateRefreshToken(User user) {
		List<RefreshToken> userTokens = refreshTokenRepository.findAllByUser(user);
		if (userTokens.size() >= maxTokensPerUser) {
			refreshTokenRepository.deleteAllByUser(user);
		}

		RefreshToken refreshToken = RefreshToken.builder()
				.user(user)
				.token(UUID.randomUUID().toString())
				.expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
				.build();

		refreshToken = refreshTokenRepository.save(refreshToken);
		user.setRefreshToken(refreshToken);
		log.info("Generated new refresh token for user {}", user.getUsername());
		return refreshToken;
	}

	public RefreshToken getRefreshTokenByValue(String refreshTokenValue) {
		return refreshTokenRepository.findByToken(refreshTokenValue).orElseThrow(() -> new JwtException("Invalid refresh token"));
	}

	@Transactional
	public void deleteRefreshToken(RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}
}
