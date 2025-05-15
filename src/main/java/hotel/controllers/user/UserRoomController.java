package hotel.controllers.user;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.room.response.RoomListResponse;
import hotel.dto.room.response.RoomResponse;
import hotel.entities.Room;
import hotel.mappers.RoomMapper;
import hotel.services.hotelServices.RoomService;
import hotel.sort.RoomSortParams;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/rooms")
@AllArgsConstructor
public class UserRoomController {
	private RoomService roomService;
	private RoomMapper roomMapper;

	@GetMapping
	public ResponseEntity<RoomListResponse> getAllRooms(@ModelAttribute RoomSortParams params) {
		List<Room> rooms = roomService.getAll(params);
		return ResponseEntity.ok(roomMapper.toRoomListResponse(rooms, roomService.getActiveCount()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
		return ResponseEntity.ok(roomMapper.toFullResponse(roomService.getRoomById(id)));
	}

	@GetMapping("/open/count")
	public ResponseEntity<Integer> getAmountOfOpenRooms() {
		return ResponseEntity.ok(roomService.getAmountOfOpenRooms());
	}

	@GetMapping("/open")
	public ResponseEntity<RoomListResponse> getListOfOpenRooms() {
		return ResponseEntity.ok().body(roomMapper.toRoomListResponse(roomService.getListOfOpenRooms(), roomService.getActiveCount()));
	}

	@GetMapping("/available") // available?checkIn=2025-04-04&checkOut=2025-05-05
	public ResponseEntity<RoomListResponse> getAvailableRoomsOnDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
		List<Room> availableRooms = roomService.getAvailableRoomsOnDate(checkIn, checkOut);
		return ResponseEntity.ok(roomMapper.toRoomListResponse(availableRooms, roomService.getActiveCount()));
	}
}