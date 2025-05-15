package hotel.services.hotelServices;

import hotel.dto.facility.request.FacilityRequest;
import hotel.entities.Facility;
import hotel.sort.FacilitySortParams;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FacilityService {
    void addAll(Map<Long, Facility> facilities);

    List<Facility> getAll(FacilitySortParams params);

    List<Facility> getAllSimple();

    Facility createFacility(Facility facility);

    void updateFacility(Facility facility);

    Facility updateFacility(Long id, FacilityRequest request);

    void deleteFacility(Facility facility);

    void setFacilityPrice(Facility facility, BigDecimal price);

    Facility getFacilityById(Long id);

    List<Facility> getFacilitiesListSortedByPrice();

    Long getActiveCount();
}
