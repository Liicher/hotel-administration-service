package hotel.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import hotel.entities.Price;
import hotel.enums.RoomType;
import hotel.exceptions.DatabaseException;
import hotel.repository.interfaces.PriceRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepository {
	private final SessionFactory sessionFactory;

	@Override
	public List<Price> findAll() {
		log.debug("Start getting all prices");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Price> query = session.createQuery("FROM Price p ", Price.class);
			log.debug("All prices gotten successfully");
			return query.list();
		} catch (HibernateException e) {
			log.warn("Failed to get all prices - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Price findByRoomType(RoomType roomType) {
		log.debug("Start getting price by room type");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Price> query = session.createQuery("FROM Price p WHERE p.roomType = :roomType", Price.class);
			query.setParameter("roomType", roomType);
			log.debug("Price gotten successfully");
			return query.uniqueResult();
		} catch (HibernateException e) {
			log.warn("Failed to get price - Exception {}", e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}
}
