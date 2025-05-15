package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.Guest;
import hotel.enums.GuestSortCriteria;
import hotel.enums.SortDirection;
import hotel.exceptions.DatabaseException;
import hotel.exceptions.NotFoundException;
import hotel.repository.interfaces.GuestRepository;
import hotel.sort.GuestSortParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GuestRepositoryImpl implements GuestRepository {
	private final Class<Guest> entityClass = Guest.class;
	private final SessionFactory sessionFactory;

	@Override
	public Guest save(Guest entity) {
		log.debug("Start adding new guest: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(entity);
			log.debug("Added guest: {}", entity);
		} catch (DatabaseException e) {
			log.warn("Failed to add guest: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
		return entity;
	}

	@Override
	public void saveAll(Collection<Guest> entities) {
		log.debug("Start saving {} guests", entities.size());
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder sql = new StringBuilder("INSERT INTO Guest (id, name, last_name, is_settled, is_active) VALUES\n");
			for (Guest entity : entities) {
				sql.append("(")
						.append(entity.getId()).append(", ")
						.append("'").append(entity.getName()).append("'").append(", ")
						.append("'").append(entity.getLastName()).append("'").append(", ")
						.append(entity.getIsSettled()).append(", ")
						.append(entity.getIsActive()).append("),\n");
			}
			sql.deleteCharAt(sql.length() - 2);
			session.createNativeQuery(sql.toString()).executeUpdate();
			log.debug("Successfully saved {} guests", entities.size());
		} catch (HibernateException e) {
			log.warn("Failed to save guests - Exception {}", e.getMessage());
			throw new DatabaseException("Failed to save guests: " + e.getMessage());
		}
	}

	@Override
	public Optional<Guest> findById(Long id) {
		log.debug("Start getting guest by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Guest> query = session.createQuery(
					"FROM Guest g " +
							"LEFT JOIN FETCH g.reservationList res " +
							"LEFT JOIN FETCH res.facilityList " +
							"LEFT JOIN FETCH res.room " +
							"WHERE g.id = :id AND g.isActive = true", entityClass);
			query.setParameter("id", id);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.warn("Failed to get guest by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<Guest> findAllSorted(GuestSortParams params) {
		log.debug("Getting all guests with sorting");
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder hql = new StringBuilder("FROM Guest g LEFT JOIN FETCH g.reservationList res WHERE g.isActive = true ORDER BY ");

			Map<GuestSortCriteria, SortDirection> sort = params.getSort();
			if (sort.isEmpty()) {
				sort = params.getDefaultSort();
			}
			sort.forEach((criteria, direction) -> {
				hql.append(criteria.getHqlField()).append(" ").append(direction.name()).append(", ");
			});
			hql.setLength(hql.length() - 2);

			Query<Guest> query = session.createQuery(hql.toString(), entityClass);
			query.setFirstResult(params.getPage() * params.getSize());
			query.setMaxResults(params.getSize());
			return query.list();
		} catch (HibernateException e) {
			log.error("Failed to get sorted guests", e);
			throw new DatabaseException("Failed to get sorted guests: " + e.getMessage());
		}
	}

	@Override
	public List<Guest> findAllSimpleInfo() {
		log.debug("Start getting all guests simple info");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Guest> query = session.createQuery("FROM Guest g WHERE g.isActive = true", entityClass);
			log.debug("All guests info gotten successfully");
			return query.list();
		} catch (HibernateException e) {
			log.warn("Failed to get all guests simple info - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Guest update(Guest entity) {
		log.debug("Start updating guest: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			Guest updatedEntity = session.merge(entity);
			log.debug("Guest: {} - successfully updated", entity);
			return updatedEntity;
		} catch (DatabaseException e) {
			log.warn("Failed to update guest: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {
		log.debug("Start deleting guest by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Guest entity = session.get(entityClass, id);
			if (entity == null) {
				throw new NotFoundException("Guest with ID: " + id + " not found");
			}
			session.delete(entity);
			log.debug("Guest with ID: {} - successfully deleted", id);
		} catch (DatabaseException e) {
			log.warn("Failed to delete guest by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Long countALl() {
		log.debug("Start counting all active guests");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Long> query = session.createQuery("SELECT COUNT(g) FROM Guest g WHERE g.isActive = true", Long.class);
			Long count = query.uniqueResult();
			log.debug("Counted active guests: {}", count);
			return count;
		} catch (HibernateException e) {
			log.warn("Failed to count active guests - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}
}
