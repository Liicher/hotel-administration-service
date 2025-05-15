package hotel.repository.interfaces;

import hotel.entities.Room;
import hotel.repository.GenericDao;
import hotel.sort.RoomSortParams;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends GenericDao<Room, Long> {
    Optional<Room> getRoomByNumber(Long roomNumber);
    List<Room> findAllSorted(RoomSortParams params);
    List<Room> findAllSimpleInfo();
    List<Room> findAllAvailableRoomsOnDate(LocalDate checkIn, LocalDate checkOut);
}