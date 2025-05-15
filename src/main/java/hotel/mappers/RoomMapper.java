package hotel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import hotel.dto.room.request.RoomRequest;
import hotel.dto.room.response.RoomListResponse;
import hotel.dto.room.response.RoomResponse;
import hotel.dto.room.response.RoomSimpleResponse;
import hotel.entities.Room;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReservationMapper.class)
public interface RoomMapper {
	Room toEntity(RoomRequest request);

	RoomSimpleResponse toSimpleResponse(Room room);

	RoomResponse toFullResponse(Room room);

	List<RoomSimpleResponse> toSimpleResponseList(List<Room> rooms);

	@Mapping(target = "id", ignore = true)
	void updateEntity(@MappingTarget Room room, RoomRequest request);

	default RoomListResponse toRoomListResponse(List<Room> responseList, Long totalCount) {
		return RoomListResponse.builder()
				.totalCount(totalCount)
				.rooms(toSimpleResponseList(responseList))
				.build();
	}
}
