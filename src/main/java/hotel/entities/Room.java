package hotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import hotel.enums.RoomStatus;
import hotel.enums.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false)
    private Long roomNumber;

    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(name = "room_status")
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @ToString.Exclude
    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "room")
    @BatchSize(size = 10)
    private List<Reservation> reservationList = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
        reservation.setRoom(this);
        if (reservation.getCheckIn().isBefore(LocalDate.now()) && reservation.getCheckOut().isAfter(LocalDate.now())) {
            roomStatus = RoomStatus.OCCUPIED;
        }
    }

    public void removeReservation(Reservation reservation) {
        reservationList.remove(reservation);
        if (reservation.getCheckIn().isBefore(LocalDate.now()) && reservation.getCheckOut().isAfter(LocalDate.now())) {
            roomStatus = RoomStatus.OPEN;
        }
    }

    public Integer getRoomCapacity() {
        return roomType.getRoomTypeMaxCapacity();
    }
}
