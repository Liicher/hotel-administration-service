package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.Facility;
import hotel.entities.Reservation;
import hotel.enums.ReservationSortCriteria;
import hotel.enums.SortDirection;
import hotel.exceptions.DatabaseException;
import hotel.exceptions.NotFoundException;
import hotel.repository.interfaces.ReservationRepository;
import hotel.sort.ReservationSortParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {
	private final Class<Reservation> entityClass = Reservation.class;
	private final SessionFactory sessionFactory;

	@Override
	public Reservation save(Reservation entity) {
		log.debug("Start adding new reservation: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(entity);
			log.debug("Added reservation: {}", entity);
		} catch (DatabaseException e) {
			log.warn("Failed to add reservation: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
		return entity;
	}

	@Override
	public void saveAll(Collection<Reservation> entities) {
		log.debug("Start saving {} reservations", entities.size());
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder sql = new StringBuilder("INSERT INTO Reservation (id, room_id, guest_id, price, check_in, check_out, is_active) VALUES\n");
			for (Reservation entity : entities) {
				sql.append("(")
						.append(entity.getId()).append(", ")
						.append(entity.getRoom().getId()).append(", ")
						.append(entity.getGuest().getId()).append(", ")
						.append(entity.getPrice()).append(", ")
						.append("'").append(entity.getCheckIn()).append("'").append(", ")
						.append("'").append(entity.getCheckOut()).append("'").append(", ")
						.append(entity.getIsActive()).append("),\n");

				if (!entity.getFacilityList().isEmpty()) {
					saveReservationFacilities(entity);
				}
			}
			sql.deleteCharAt(sql.length() - 2);
			session.createNativeQuery(sql.toString()).executeUpdate();
			log.debug("Successfully saved {} reservations", entities.size());
		} catch (HibernateException e) {
			log.warn("Failed to save reservations - Exception {}", e.getMessage());
			throw new DatabaseException("Failed to save reservations: " + e.getMessage());
		}
	}

	private void saveReservationFacilities(Reservation reservation) {
		log.debug("Start saving facilities of reservation {}", reservation.getId());
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder sql = new StringBuilder("INSERT INTO reservation_facility (reservation_id, facility_id) VALUES\n");
			for (Facility facility : reservation.getFacilityList()) {
				sql.append("(").append(reservation.getId()).append(", ").append(facility.getId()).append("),\n");
			}
			sql.deleteCharAt(sql.length() - 2);
			session.createNativeQuery(sql.toString()).executeUpdate();
			log.debug("Successfully saved {} facilities to reservation {}", reservation.getFacilityList().size(), reservation.getId());
		} catch (HibernateException e) {
			log.warn("Failed to save facilities to reservation {} - Exception {}", reservation.getId(), e.getMessage());
			throw new DatabaseException("Failed to save facilities to reservation - Exception " + e.getMessage());
		}
	}

	@Override
	public Optional<Reservation> findById(Long id) {
		log.debug("Start getting reservation by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Reservation> query = session.createQuery(
					"FROM Reservation r " +
							"LEFT JOIN FETCH r.guest " +
							"LEFT JOIN FETCH r.facilityList " +
							"LEFT JOIN FETCH r.room " +
							"WHERE r.id = :id AND r.isActive = true", entityClass
			);
			query.setParameter("id", id);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.warn("Failed to get reservation by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Optional<Reservation> getReservationByRoomNumber(Long roomNumber) {
		log.debug("Start getting reservation by room number: {}", roomNumber);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Reservation> query = session.createQuery(
					"FROM Reservation r " +
							"LEFT JOIN FETCH r.guest " +
							"LEFT JOIN FETCH r.facilityList " +
							"WHERE r.room.roomNumber = :roomNumber AND r.isActive = true", entityClass
			);
			query.setParameter("roomNumber", roomNumber);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.warn("Failed to get reservation by room number: {} - Exception {}", roomNumber, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<Reservation> findAllSorted(ReservationSortParams params) {
		log.debug("Getting all reservations with sorting");
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder hql = new StringBuilder("FROM Reservation r " +
					"LEFT JOIN FETCH r.room " +
					"LEFT JOIN FETCH r.guest " +
					"LEFT JOIN FETCH r.facilityList " +
					"WHERE r.isActive = true ORDER BY ");

			Map<ReservationSortCriteria, SortDirection> sort = params.getSort();
			if (sort.isEmpty()) {
				sort = params.getDefaultSort();
			}
			sort.forEach((criteria, direction) -> {
				hql.append(criteria.getHqlField()).append(" ").append(direction.name()).append(", ");
			});
			hql.setLength(hql.length() - 2);

			Query<Reservation> query = session.createQuery(hql.toString(), entityClass);
			query.setFirstResult(params.getPage() * params.getSize());
			query.setMaxResults(params.getSize());
			return query.list();
		} catch (HibernateException e) {
			log.error("Failed to get sorted reservations", e);
			throw new DatabaseException("Failed to get sorted reservations: " + e.getMessage());
		}
	}

	@Override
	public Reservation update(Reservation entity) {
		log.debug("Start updating reservation: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			Reservation updatedEntity = session.merge(entity);
			log.debug("Reservation: {} - successfully updated", entity);
			return updatedEntity;
		} catch (DatabaseException e) {
			log.warn("Failed to update reservation: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {
		log.debug("Start deleting reservation by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Reservation entity = session.get(entityClass, id);
			if (entity == null) {
				throw new NotFoundException("Reservation with ID: " + id + " not found");
			}
			session.delete(entity);
			log.debug("Reservation with ID: {} - successfully deleted", id);
		} catch (DatabaseException e) {
			log.warn("Failed to delete reservation by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Long countALl() {
		log.debug("Start counting all active reservations");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Long> query = session.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.isActive = true", Long.class);
			Long count = query.uniqueResult();
			log.debug("Counted active reservations: {}", count);
			return count;
		} catch (HibernateException e) {
			log.warn("Failed to count active reservations - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}
}
