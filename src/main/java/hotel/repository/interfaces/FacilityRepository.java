package hotel.repository.interfaces;

import hotel.entities.Facility;
import hotel.repository.GenericDao;
import hotel.sort.FacilitySortParams;

import java.util.List;

public interface FacilityRepository extends GenericDao<Facility, Long> {
	List<Facility> findAllSorted(FacilitySortParams params);
	List<Facility> findAllSimpleInfo();
}