package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.Facility;
import hotel.enums.FacilitySortCriteria;
import hotel.enums.SortDirection;
import hotel.exceptions.DatabaseException;
import hotel.exceptions.NotFoundException;
import hotel.repository.interfaces.FacilityRepository;
import hotel.sort.FacilitySortParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityRepository {
	private Class<Facility> entityClass = Facility.class;
	private final SessionFactory sessionFactory;

	@Override
	public Facility save(Facility entity) {
		log.debug("Start adding new facility: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(entity);
			log.debug("Added facility: {}", entity);
		} catch (HibernateException e) {
			log.warn("Failed to add facility: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
		return entity;
	}

	@Override
	public void saveAll(Collection<Facility> entities) {
		log.debug("Start saving {} facilities", entities.size());
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder sql = new StringBuilder("INSERT INTO Facility (id, facility_name, facility_price, is_active) VALUES\n");
			for (Facility entity : entities) {
				sql.append("(")
						.append(entity.getId()).append(", ")
						.append("'").append(entity.getFacilityName()).append("'").append(", ")
						.append(entity.getFacilityPrice()).append(", ")
						.append(entity.getIsActive()).append("),\n");
			}
			sql.deleteCharAt(sql.length() - 2);
			session.createNativeQuery(sql.toString()).executeUpdate();
			log.debug("Successfully saved {} facilities", entities.size());
		} catch (HibernateException e) {
			log.warn("Failed to save facilities - Exception {}", e.getMessage());
			throw new DatabaseException("Failed to save facilities: " + e.getMessage());
		}
	}

	@Override
	public Optional<Facility> findById(Long id) {
		log.debug("Start getting facility by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Facility> query = session.createQuery(
					"FROM Facility f LEFT JOIN FETCH f.reservationList WHERE f.id = :id AND f.isActive = true",
					entityClass);
			query.setParameter("id", id);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.warn("Failed to get facility by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<Facility> findAllSorted(FacilitySortParams params) {
		log.debug("Getting all facilities with sorting");
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder hql = new StringBuilder("FROM Facility f LEFT JOIN FETCH f.reservationList res WHERE f.isActive = true ORDER BY ");

			Map<FacilitySortCriteria, SortDirection> sort = params.getSort();
			if (sort.isEmpty()) {
				sort = params.getDefaultSort();
			}
			sort.forEach((criteria, direction) -> {
				hql.append(criteria.getHqlField()).append(" ").append(direction.name()).append(", ");
			});
			hql.setLength(hql.length() - 2);

			Query<Facility> query = session.createQuery(hql.toString(), entityClass);
			query.setFirstResult(params.getPage() * params.getSize());
			query.setMaxResults(params.getSize());
			return query.list();
		} catch (HibernateException e) {
			log.error("Failed to get sorted facilities", e);
			throw new DatabaseException("Failed to get sorted facilities: " + e.getMessage());
		}
	}

	@Override
	public List<Facility> findAllSimpleInfo() {
		log.debug("Start getting all facilities simple info");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Facility> query = session.createQuery("FROM Facility f WHERE f.isActive = true", entityClass);
			log.debug("All facilities info gotten successfully");
			return query.list();
		} catch (HibernateException e) {
			log.warn("Failed to get all Facilities simple info - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Facility update(Facility entity) {
		log.debug("Start updating facility: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			Facility updatedEntity = session.merge(entity);
			log.debug("Facility: {} - successfully updated", entity);
			return updatedEntity;
		} catch (HibernateException e) {
			log.warn("Failed to update facility: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {
		log.debug("Start deleting facility by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Facility entity = session.get(entityClass, id);
			if (entity == null) {
				throw new NotFoundException("Facility with ID: " + id + " not found");
			}
			session.delete(entity);
			log.debug("Facility with ID: {} - successfully deleted", id);
		} catch (HibernateException e) {
			log.warn("Failed to delete facility by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Long countALl() {
		log.debug("Start counting all active facilities");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Long> query = session.createQuery("SELECT COUNT(f) FROM Facility f WHERE f.isActive = true", Long.class);
			Long count = query.uniqueResult();
			log.debug("Counted active facilities: {}", count);
			return count;
		} catch (HibernateException e) {
			log.warn("Failed to count active facilities - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}
}
