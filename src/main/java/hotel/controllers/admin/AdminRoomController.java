package hotel.controllers.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hotel.dto.room.request.RoomRequest;
import hotel.dto.room.response.RoomResponse;
import hotel.entities.Room;
import hotel.mappers.RoomMapper;
import hotel.services.hotelServices.RoomService;

@RestController
@RequestMapping("/api/v1/admin/rooms")
@AllArgsConstructor
public class AdminRoomController {
	private RoomService roomService;
	private RoomMapper roomMapper;

	@PostMapping
	public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
		Room createdRoom = roomService.createRoom(roomMapper.toEntity(request));
		return new ResponseEntity<>(roomMapper.toFullResponse(createdRoom), HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
		Room updatedRoom = roomService.update(id, request);
		return ResponseEntity.ok(roomMapper.toFullResponse(updatedRoom));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
		roomService.deleteRoom(id);
		return ResponseEntity.noContent().build();
	}
}