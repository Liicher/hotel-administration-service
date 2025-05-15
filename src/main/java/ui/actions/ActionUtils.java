package ui.actions;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.entities.Room;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ActionUtils {
    private final String dateRegex = "(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.20[2-9][4-9]";
    private final Scanner scanner = new Scanner(System.in);

    private RoomService roomService;
    private GuestService guestService;

    public String stringInput() {
        String input = scanner.next().trim();
        while (input.isBlank()) {
            input = scanner.next().trim();
        }
        return input;
    }

    public String nameInput() {
        String input = scanner.next().trim();
        while (input.isBlank() || !input.matches("^[A-Z][a-z]+")) {
            input = scanner.next().trim();
        }
        return input;
    }

    public Long idInput() {
        String input = scanner.next().trim();
        while (!input.matches("\\d+")) {
            input = scanner.next().trim();
        }
        return Long.parseLong(input);
    }

    public Long newRoomNumberInput() {
        List<Room> rooms = roomService.getAllSimple();
        rooms.forEach(v -> System.out.println(v.toString()));
        Set<Long> existingNumbers = rooms.stream().map(Room::getRoomNumber).collect(Collectors.toSet());
        System.out.println("Enter NEW room's number (4 digits):");
        String input = scanner.next().trim();
        while (!input.matches("\\d{4}") || existingNumbers.contains(Long.parseLong(input))) {
            System.out.println("Invalid input! Enter NEW room's number (4 digits):");
            input = scanner.next().trim();
        }
        return Long.parseLong(input);
    }

    public Long roomNumberInput() {
        List<Room> rooms = roomService.getAllSimple();
        rooms.forEach(v -> System.out.println(v.toString()));
        List<Long> numbers = rooms.stream().map(Room::getRoomNumber).toList();
        System.out.println("Enter room's number (4 digits):");
        String input = scanner.next().trim();
        while (!input.matches("\\d{4}") || !numbers.contains(Long.parseLong(input))) {
            System.out.println("Invalid input! Enter room's number (4 digits):");
            input = scanner.next().trim();
        }
        return Long.parseLong(input);
    }

    public Long roomNumberInput(List<Room> rooms) {
        List<Long> numbers = rooms.stream().map(Room::getRoomNumber).collect(Collectors.toList());
        rooms.forEach(v -> System.out.println(v.toString()));
        System.out.println("Enter room's number (4 digits):");
        String input = scanner.next().trim();
        while (!input.matches("\\d{4}") || !numbers.contains(Long.parseLong(input))) {
            System.out.println("Invalid input! Enter room's number from list:");
            input = scanner.next().trim();
        }
        return Long.parseLong(input);
    }

    public RoomType roomTypeInput() {
        System.out.println("Room types:");
        List<RoomType> roomTypeList = List.of(RoomType.values());
        for (int i = 0; i < roomTypeList.size(); i++) {
            System.out.println("- " + (i + 1) + ". " + roomTypeList.get(i));
        }
        System.out.println("Choose the type of room: ");
        String input = scanner.next().trim();
        while (!input.matches("\\d+")
                && Integer.parseInt(input) < 0
                && Integer.parseInt(input) > roomTypeList.size()) {
            input = scanner.next().trim();
        }
        return roomTypeList.get(Integer.parseInt(input) - 1);
    }

    public RoomStatus roomStatusInput() {
        System.out.println("Room status:");
        List<RoomStatus> roomStatusesList = List.of(RoomStatus.values());
        for (int i = 0; i < roomStatusesList.size(); i++) {
            System.out.println("- " + (i + 1) + ". " + roomStatusesList.get(i));
        }
        System.out.println("Choose the status of room: ");
        String input = scanner.next().trim();
        while (!input.matches("\\d+")
                && Integer.parseInt(input) < 0
                && Integer.parseInt(input) > roomStatusesList.size()) {
            input = scanner.next().trim();
        }
        return roomStatusesList.get(Integer.parseInt(input) - 1);
    }

    public BigDecimal priceInput() {
        System.out.println("Enter price (Format - 0.00): ");
        String input = scanner.next().trim();
        while (!input.matches("^\\d+\\.\\d{2}$")) {
            input = scanner.next().trim();
        }
        return BigDecimal.valueOf(Double.parseDouble(input));
    }

    public LocalDate dateInput() {
        String input = scanner.next().trim();
        while (!input.matches(dateRegex)) {
            input = scanner.next().trim();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(input, formatter);
    }

    public LocalDate checkOutDateInput(LocalDate checkInDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String input = scanner.next().trim();
        while (!input.matches(dateRegex) || LocalDate.parse(input, formatter).isBefore(checkInDate)) {
            input = scanner.next().trim();
        }
        return LocalDate.parse(input, formatter);
    }

    public Guest getGuestForReservation() {
        List<Guest> allGuests = guestService.getSortedListOfAllGuests();
        Guest guest = null;

        System.out.println("List of guests:");
        allGuests.forEach((v) -> System.out.println(v.toString()));
        String input = scanner.next().trim();
        while (guest == null) {
            if (input.matches("\\d+")) {
                Long id = Long.parseLong(input);
                guest = guestService.getGuestById(id);
                continue;
            }
            input = scanner.next().trim();
        }
        return guest;
    }

    public Reservation getReservation() {
        Room room = roomService.getRoomByNumber(roomNumberInput());
        List<Reservation> roomReservations = room.getReservationList();
        roomReservations.forEach(System.out::println);
        System.out.println("Enter reservation id: ");
        Long reservationId = idInput();
        return roomReservations.stream()
                .filter(v -> v.getId().equals(reservationId))
                .findFirst().get();
    }
}
