package hotel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import hotel.dto.guest.request.GuestRequest;
import hotel.dto.guest.response.GuestListResponse;
import hotel.dto.guest.response.GuestResponse;
import hotel.dto.guest.response.GuestSimpleResponse;
import hotel.entities.Guest;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReservationMapper.class)
public interface GuestMapper {
	Guest toEntity(GuestRequest request);

	GuestSimpleResponse toSimpleResponse(Guest guest);

	GuestResponse toFullResponse(Guest guest);

	List<GuestSimpleResponse> toSimpleResponseList(List<Guest> guests);

	@Mapping(target = "id", ignore = true)
	void updateEntity(@MappingTarget Guest guest, GuestRequest request);

	default GuestListResponse toGuestListResponse(List<Guest> responseList, Long totalCount) {
		return GuestListResponse.builder()
				.totalCount(totalCount)
				.guests(toSimpleResponseList(responseList))
				.build();
	}
}
