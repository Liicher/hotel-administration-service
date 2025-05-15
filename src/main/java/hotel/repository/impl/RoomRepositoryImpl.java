package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.Room;
import hotel.enums.RoomSortCriteria;
import hotel.enums.SortDirection;
import hotel.exceptions.DatabaseException;
import hotel.exceptions.NotFoundException;
import hotel.repository.interfaces.RoomRepository;
import hotel.sort.RoomSortParams;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {
	private final Class<Room> entityClass = Room.class;
	private final SessionFactory sessionFactory;

	@Override
	public Room save(Room entity) {
		log.debug("Start adding new room: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			session.saveOrUpdate(entity);
			log.debug("Added room: {}", entity);
		} catch (DatabaseException e) {
			log.warn("Failed to add room: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
		return entity;
	}

	@Override
	public void saveAll(Collection<Room> entities) {
		log.debug("Start saving {} rooms", entities.size());
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder sql = new StringBuilder("INSERT INTO Room (id, room_number, room_type, room_status, is_active) VALUES\n");
			for (Room entity : entities) {
				sql.append("(")
						.append(entity.getId()).append(", ")
						.append(entity.getRoomNumber()).append(", ")
						.append("'").append(entity.getRoomType()).append("'").append(", ")
						.append("'").append(entity.getRoomStatus()).append("'").append(", ")
						.append(entity.getIsActive()).append("),\n");
			}
			sql.deleteCharAt(sql.length() - 2);
			session.createNativeQuery(sql.toString()).executeUpdate();
			log.debug("Successfully saved {} rooms", entities.size());
		} catch (HibernateException e) {
			log.warn("Failed to save rooms - Exception {}", e.getMessage());
			throw new DatabaseException("Failed to save rooms: " + e.getMessage());
		}
	}

	@Override
	public Optional<Room> findById(Long id) {
		log.debug("Start getting room by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Room> query = session.createQuery(
					"FROM Room r " +
							"LEFT JOIN FETCH r.reservationList res " +
							"LEFT JOIN FETCH res.guest " +
							"LEFT JOIN FETCH res.facilityList " +
							"WHERE r.id = :id AND r.isActive = true", entityClass);
			query.setParameter("id", id);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.warn("Failed to get room by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Optional<Room> getRoomByNumber(Long roomNumber) {
		log.debug("Start getting room by roomNumber: {}", roomNumber);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Room> query = session.createQuery(
					"FROM Room r " +
							"LEFT JOIN FETCH r.reservationList res " +
							"LEFT JOIN FETCH res.guest g " +
							"LEFT JOIN FETCH res.facilityList " +
							"WHERE r.roomNumber = :roomNumber AND r.isActive = true", entityClass);
			query.setParameter("roomNumber", roomNumber);
			return Optional.ofNullable(query.uniqueResult());
		} catch (HibernateException e) {
			log.warn("Failed to get room by roomNumber: {} - Exception {}", roomNumber, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<Room> findAllSorted(RoomSortParams params) {
		log.debug("Getting all rooms with sorting");
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder hql = new StringBuilder("FROM Room r LEFT JOIN FETCH r.reservationList res WHERE r.isActive = true ORDER BY ");

			Map<RoomSortCriteria, SortDirection> sort = params.getSort();
			if (sort.isEmpty()) {
				sort = params.getDefaultSort();
			}
			sort.forEach((criteria, direction) -> {
				hql.append(criteria.getHqlField()).append(" ").append(direction.name()).append(", ");
			});
			hql.setLength(hql.length() - 2);

			Query<Room> query = session.createQuery(hql.toString(), entityClass);
			query.setFirstResult(params.getPage() * params.getSize());
			query.setMaxResults(params.getSize());
			return query.list();
		} catch (HibernateException e) {
			log.error("Failed to get sorted rooms", e);
			throw new DatabaseException("Failed to get sorted rooms: " + e.getMessage());
		}
	}

	@Override
	public List<Room> findAllSimpleInfo() {
		log.debug("Start getting simple rooms info");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Room> query = session.createQuery(
					"FROM Room r WHERE r.isActive = true", entityClass);
			return query.list();
		} catch (HibernateException e) {
			log.warn("Failed to get simple rooms info - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<Room> findAllAvailableRoomsOnDate(LocalDate checkIn, LocalDate checkOut) {
		log.debug("Start getting all available rooms from {} to {}", checkIn, checkOut);
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Room> query = session.createQuery(
					"SELECT DISTINCT r FROM Room r " +
							"LEFT JOIN FETCH r.reservationList res " +
							"LEFT JOIN FETCH res.guest " +
							"LEFT JOIN FETCH res.facilityList " +
							"WHERE r.isActive = true " +
							"AND (r.roomStatus = 'OPEN' OR r.roomStatus = 'OCCUPIED') " +
							"AND (res IS NULL OR NOT (res.checkIn <= :checkOut AND res.checkOut >= :checkIn))", entityClass);

			query.setParameter("checkIn", checkIn);
			query.setParameter("checkOut", checkOut);

			return query.list();
		} catch (HibernateException e) {
			log.warn("Failed to get available rooms from {} to {} - Exception {}",
					checkIn, checkOut, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Room update(Room entity) {
		log.debug("Start updating room: {}", entity);
		try {
			Session session = sessionFactory.getCurrentSession();
			Room updatedEntity = session.merge(entity);
			log.debug("Room: {} - successfully updated", entity);
			return updatedEntity;
		} catch (HibernateException e) {
			log.warn("Failed to update room: {} - Exception {}", entity, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {
		log.debug("Start deleting room by id: {}", id);
		try {
			Session session = sessionFactory.getCurrentSession();
			Room entity = session.get(entityClass, id);
			if (entity == null) {
				throw new NotFoundException("Room with ID: " + id + " not found");
			}
			session.delete(entity);
			log.debug("Room with ID: {} - successfully deleted", id);
		} catch (HibernateException e) {
			log.warn("Failed to delete room by id: {} - Exception {}", id, e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Long countALl() {
		log.debug("Start counting all active rooms");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Long> query = session.createQuery("SELECT COUNT(r) FROM Room r WHERE r.isActive = true", Long.class);
			Long count = query.uniqueResult();
			log.debug("Counted active rooms: {}", count);
			return count;
		} catch (HibernateException e) {
			log.warn("Failed to count active rooms - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}
}

