package hotel.repository.interfaces;

import hotel.entities.Guest;
import hotel.repository.GenericDao;
import hotel.sort.GuestSortParams;

import java.util.List;

public interface GuestRepository extends GenericDao<Guest, Long> {
	List<Guest> findAllSorted(GuestSortParams params);
	List<Guest> findAllSimpleInfo();
}