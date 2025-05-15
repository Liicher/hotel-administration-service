package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.security.RefreshToken;
import hotel.entities.security.User;
import hotel.exceptions.DatabaseException;
import hotel.repository.interfaces.RefreshTokenRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
	private final SessionFactory sessionFactory;

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<RefreshToken> query = session.createQuery(
					"FROM RefreshToken rt WHERE rt.token = :token",
					RefreshToken.class
			);
			query.setParameter("token", token);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.error("Failed to find refresh token", e);
			throw new DatabaseException("Failed to find refresh token");
		}
	}

	@Override
	public RefreshToken save(RefreshToken refreshToken) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.persist(refreshToken);
			return refreshToken;
		} catch (HibernateException e) {
			log.error("Failed to save refresh token", e);
			throw new DatabaseException("Failed to save refresh token");
		}
	}

	@Override
	public void delete(RefreshToken refreshToken) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.remove(refreshToken);
		} catch (HibernateException e) {
			log.error("Failed to delete refresh token", e);
			throw new DatabaseException("Failed to delete refresh token");
		}
	}

	@Override
	public void deleteAllByUser(User user) {
		try {
			log.info("Start to delete all refresh tokens for user {}", user.getUsername());
			Session session = sessionFactory.getCurrentSession();
			Query<?> query = session.createQuery("DELETE FROM RefreshToken rt WHERE rt.user = :user");
			query.setParameter("user", user);
			query.executeUpdate();
			log.info("Deleted refresh tokens for user {}", user.getUsername());
		} catch (HibernateException e) {
			log.error("Failed to delete all refresh tokens for user {}", user.getUsername(), e);
			throw new DatabaseException("Failed to delete all refresh tokens for user: " + user.getUsername());
		}
	}

	@Override
	public List<RefreshToken> findAllByUser(User user) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<RefreshToken> query = session.createQuery(
					"FROM RefreshToken rt WHERE rt.user = :user ORDER BY rt.expiryDate",
					RefreshToken.class
			);
			query.setParameter("user", user);
			return query.getResultList();
		} catch (HibernateException e) {
			log.error("Failed to find refresh tokens for user {}", user.getUsername(), e);
			throw new DatabaseException("Failed to find refresh tokens for user");
		}
	}
}
