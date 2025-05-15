package hotel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import hotel.dto.reservation.request.ReservationRequest;
import hotel.dto.reservation.response.ReservationListResponse;
import hotel.dto.reservation.response.ReservationResponse;
import hotel.dto.reservation.response.ReservationSimpleResponse;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.repository.interfaces.GuestRepository;
import hotel.repository.interfaces.RoomRepository;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FacilityMapper.class, RoomMapper.class, GuestMapper.class})
public abstract class ReservationMapper {
	@Autowired
	private GuestRepository guestRepository;
	@Autowired
	private RoomRepository roomRepository;

	@Mapping(target = "guest", source = "guestId")
	@Mapping(target = "room", source = "roomId")
	public abstract Reservation toEntity(ReservationRequest request);

	public abstract ReservationSimpleResponse toSimpleResponse(Reservation reservation);

	public abstract ReservationResponse toFullResponse(Reservation reservation);

	public abstract List<ReservationSimpleResponse> toSimpleResponseList(List<Reservation> reservations);

	@Mapping(target = "id", ignore = true)
	public abstract void updateEntity(@MappingTarget Reservation reservation, ReservationRequest request);

	public ReservationListResponse toReservationListResponse(List<Reservation> responseList, Long totalCount) {
		return ReservationListResponse.builder()
				.totalCount(totalCount)
				.reservations(toSimpleResponseList(responseList))
				.build();
	}

	protected Guest mapGuestId(Long guestId) {
		if (guestId == null) {
			return null;
		}
		return guestRepository.findById(guestId).orElseThrow();
	}

	protected Room mapRoomId(Long roomId) {
		if (roomId == null) {
			return null;
		}
		return roomRepository.findById(roomId).orElseThrow();
	}
}
