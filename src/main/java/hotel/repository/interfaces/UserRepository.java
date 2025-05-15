package hotel.repository.interfaces;

import hotel.entities.security.User;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findByUsername(String username);
	User findById(Long id);
	User save(User user);
	Boolean existsByUsername(String username);
}
