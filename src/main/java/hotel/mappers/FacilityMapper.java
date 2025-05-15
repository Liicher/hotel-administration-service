package hotel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import hotel.dto.facility.request.FacilityRequest;
import hotel.dto.facility.response.FacilityListResponse;
import hotel.dto.facility.response.FacilityResponse;
import hotel.dto.facility.response.FacilitySimpleResponse;
import hotel.entities.Facility;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReservationMapper.class)
public interface FacilityMapper {
	Facility toEntity(FacilityRequest request);

	FacilitySimpleResponse toSimpleResponse(Facility facility);

	FacilityResponse toFullResponse(Facility facility);

	List<FacilitySimpleResponse> toSimpleResponseList(List<Facility> facilities);

	@Mapping(target = "id", ignore = true)
	void updateEntity(@MappingTarget Facility facility, FacilityRequest request);

	default FacilityListResponse toFacilityListResponse(List<Facility> responseList, Long totalCount) {
		return FacilityListResponse.builder()
				.totalCount(totalCount)
				.facilities(toSimpleResponseList(responseList))
				.build();
	}
}
