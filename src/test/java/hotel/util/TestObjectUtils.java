package senla.education.hotel.util;

import hotel.dto.user.RegisterRequest;
import hotel.entities.*;
import hotel.entities.security.RefreshToken;
import hotel.entities.security.Role;
import hotel.entities.security.RoleType;
import hotel.entities.security.User;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.services.security.CustomUserDetails;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestObjectUtils {

	public static Map<RoomType, BigDecimal> setRoomPricesMap() {
		Map<RoomType, BigDecimal> roomPrices = new HashMap<>();
		roomPrices.put(STANDARD, BigDecimal.valueOf(100));
		roomPrices.put(SUPERIOR, BigDecimal.valueOf(200));
		roomPrices.put(BEDROOM, BigDecimal.valueOf(300));
		roomPrices.put(LUXURY, BigDecimal.valueOf(400));
		return roomPrices;
	}

	public static Room createTestRoom(Long id, Long roomNumber) {
		return Room.builder()
				.id(id)
				.roomNumber(roomNumber)
				.roomStatus(RoomStatus.OPEN)
				.roomType(RoomType.STANDARD)
				.build();
	}

	public static Guest createTestGuest(Long id) {
		return Guest.builder()
				.id(id)
				.name("John")
				.lastName("Doe")
				.isSettled(false)
				.build();
	}

	public static Facility createTestFacility(Long id) {
		return Facility.builder()
				.id(id)
				.facilityName("TestFacilityName")
				.facilityPrice(BigDecimal.valueOf(100.00))
				.build();
	}

	public static Reservation createTestReservation(Long id) {
		return Reservation.builder()
				.id(id)
				.build();
	}

	public static Reservation createPastReservation(Long id) {
		return Reservation.builder()
				.id(id)
				.checkIn(LocalDate.now().minusDays(4))
				.checkOut(LocalDate.now().minusDays(2))
				.build();
	}

	public static Reservation createCurrentReservation(Long id) {
		return Reservation.builder()
				.id(id)
				.checkIn(LocalDate.now().minusDays(1))
				.checkOut(LocalDate.now().plusDays(1))
				.build();
	}

	public static Reservation createFutureReservation(Long id) {
		return Reservation.builder()
				.id(id)
				.checkIn(LocalDate.now().plusDays(2))
				.checkOut(LocalDate.now().plusDays(4))
				.build();
	}

	public static Reservation createFilledReservation(Long id) {
		return Reservation.builder()
				.id(id)
				.room(createTestRoom(1L, 1001L))
				.guest(createTestGuest(1L))
				.checkIn(LocalDate.of(2025, 1, 1))
				.checkOut(LocalDate.of(2025, 2, 2))
				.price(BigDecimal.valueOf(100.00))
				.build();
	}

	public static Price createTestPrice() {
		return Price.builder()
				.id(1L)
				.roomType(STANDARD)
				.roomTypePrice(BigDecimal.valueOf(100))
				.build();
	}

	public static User createTestUser() {
		return User.builder()
				.id(1L)
				.username("testUser")
				.password("testPass")
				.firstName("Test")
				.lastName("User")
				.email("test@example.com")
				.role(createTestRole())
				.build();
	}

	public static CustomUserDetails createTestUserDetails() {
		return CustomUserDetails.builder()
				.user(createTestUser())
				.build();
	}

	public static RegisterRequest createTestRegisterRequest() {
		return RegisterRequest.builder()
				.username("testUser")
				.password("testPass")
				.firstName("Test")
				.lastName("User")
				.email("test@example.com")
				.build();
	}

	public static Role createTestRole() {
		return Role.builder()
				.id(1L)
				.roleType(RoleType.ROLE_USER)
				.build();
	}

	public static RefreshToken createTestRefreshToken(User user) {
		return RefreshToken.builder()
				.id(1L)
				.user(user)
				.token("refreshToken")
				.expiryDate(Instant.now().plusSeconds(5000))
				.build();
	}
}
