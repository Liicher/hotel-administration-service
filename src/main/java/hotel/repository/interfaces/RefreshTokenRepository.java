package hotel.repository.interfaces;

import hotel.entities.security.RefreshToken;
import hotel.entities.security.User;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
	Optional<RefreshToken> findByToken(String token);
	RefreshToken save(RefreshToken refreshToken);
	void delete(RefreshToken refreshToken);
	void deleteAllByUser(User user);
	List<RefreshToken> findAllByUser(User user);
}
