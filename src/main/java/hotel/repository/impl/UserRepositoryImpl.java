package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.security.User;
import hotel.exceptions.DatabaseException;
import hotel.repository.interfaces.UserRepository;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final SessionFactory sessionFactory;
	private final Class<User> entityClass = User.class;

	@Override
	public Optional<User> findByUsername(String username) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<User> query = session.createQuery(
					"FROM User u LEFT JOIN FETCH u.role WHERE u.username = :username AND u.isActive = true",
					entityClass
			);
			query.setParameter("username", username);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.error(e.getMessage());
			throw new DatabaseException("Failed to find user by username: " + username);
		}
	}

	@Override
	public User save(User user) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(user);
			return user;
		} catch (HibernateException e) {
			log.error(e.getMessage());
			throw new DatabaseException("Failed to save user - " + e.getMessage() + "\n---\n" + Arrays.toString(e.getStackTrace()));
		}
	}

	@Override
	public User findById(Long id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			return session.get(entityClass, id);
		} catch (HibernateException e) {
			log.error(e.getMessage());
			throw new DatabaseException("Failed to find user by id: " + id);
		}
	}

	@Override
	public Boolean existsByUsername(String username) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Long> query = session.createQuery(
					"SELECT COUNT(u) FROM User u WHERE u.username = :username",
					Long.class
			);
			query.setParameter("username", username);
			return query.uniqueResult() > 0;
		} catch (HibernateException e) {
			log.error(e.getMessage());
			throw new DatabaseException("Failed to check if user exists by username: " + username + "\n" + Arrays.toString(e.getStackTrace()) + "\n" + e.getMessage());
		}
	}
}
