package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.security.Role;
import hotel.entities.security.RoleType;
import hotel.exceptions.DatabaseException;
import hotel.repository.interfaces.RoleRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
	private final SessionFactory sessionFactory;

	@Override
	public Optional<Role> findByRoleType(RoleType roleType) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Role> query = session.createQuery(
					"FROM Role r WHERE r.roleType = :roleType",
					Role.class
			);
			query.setParameter("roleType", roleType);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.error("Failed to find role by type: {}", roleType, e);
			throw new DatabaseException("Failed to find role by type: " + roleType);
		}
	}

	@Override
	public Role save(Role role) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.persist(role);
			return role;
		} catch (HibernateException e) {
			log.error("Failed to save role: {}", role, e);
			throw new DatabaseException("Failed to save role: " + e.getMessage());
		}
	}

	@Override
	public Optional<Role> findById(Long id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			return Optional.ofNullable(session.get(Role.class, id));
		} catch (HibernateException e) {
			log.error("Failed to find role by id: {}", id, e);
			throw new DatabaseException("Failed to find role by id: " + id);
		}
	}

	@Override
	public List<Role> findAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Role> query = session.createQuery("FROM Role", Role.class);
			return query.getResultList();
		} catch (HibernateException e) {
			log.error("Failed to find all roles", e);
			throw new DatabaseException("Failed to find all roles");
		}
	}
}