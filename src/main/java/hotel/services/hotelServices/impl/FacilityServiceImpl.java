package hotel.services.hotelServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.dto.facility.request.FacilityRequest;
import hotel.entities.Facility;
import hotel.exceptions.NotFoundException;
import hotel.mappers.FacilityMapper;
import hotel.repository.interfaces.FacilityRepository;
import hotel.services.hotelServices.FacilityService;
import hotel.sort.FacilitySortParams;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
	private final FacilityRepository facilityRepository;
	private final FacilityMapper facilityMapper;

	@Override
	@Transactional
	public void addAll(Map<Long, Facility> facilities) {
		facilityRepository.saveAll(facilities.values());
	}

	@Override
	@Transactional
	public List<Facility> getAll(FacilitySortParams params) {
		return facilityRepository.findAllSorted(params);
	}

	@Override
	@Transactional
	public List<Facility> getAllSimple() {
		return facilityRepository.findAllSimpleInfo();
	}

	@Override
	@Transactional
	public Facility createFacility(Facility facility) {
		return facilityRepository.save(facility);
	}

	@Override
	@Transactional
	public void updateFacility(Facility facility) {
		facilityRepository.update(facility);
	}

	@Override
	@Transactional
	public Facility updateFacility(Long id, FacilityRequest request) {
		Facility existingFacility = getFacilityById(id);
		facilityMapper.updateEntity(existingFacility, request);
		return facilityRepository.update(existingFacility);
	}

	@Override
	@Transactional
	public void deleteFacility(Facility facility) {
		facility.setIsActive(false);
		facilityRepository.update(facility);
	}

	@Override
	public void setFacilityPrice(Facility facility, BigDecimal price) {
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(String.format("%s\t-\tPrice couldn't be lower than 0!", getClass().getName()));
		}
		facility.setFacilityPrice(price);
	}

	@Override
	@Transactional
	public Facility getFacilityById(Long id) {
		return facilityRepository.findById(id).orElseThrow(() -> new NotFoundException(
						String.format("%s\t-\tFacility with id - %d doesn't exist", getClass().getName(), id)));
	}

	@Override
	@Transactional
	public List<Facility> getFacilitiesListSortedByPrice() {
		List<Facility> facilities = getAllSimple();
		Collections.sort(facilities);
		return facilities;
	}

	@Override
	@Transactional
	public Long getActiveCount() {
		return facilityRepository.countALl();
	}
}
