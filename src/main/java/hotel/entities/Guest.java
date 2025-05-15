package hotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "guest")
public class Guest implements Comparable<Guest> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(name = "is_settled", nullable = false)
    private Boolean isSettled = false;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "guest")
    @BatchSize(size = 10)
    private List<Reservation> reservationList = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
        reservation.setGuest(this);
        if (reservation.getCheckIn().isBefore(LocalDate.now()) && reservation.getCheckOut().isAfter(LocalDate.now())) {
            isSettled = true;
        }
    }

    public void removeReservation(Reservation reservation) {
        reservationList.remove(reservation);
        isSettled = reservationList.stream()
                .anyMatch(r -> r.getCheckIn().isBefore(LocalDate.now())
                        && r.getCheckOut().isAfter(LocalDate.now()));
    }

    @Override
    public int compareTo(Guest guest) {
        int result = this.lastName.compareTo(guest.lastName);
        if (result == 0) {
            result = this.name.compareTo(guest.name);
        }
        if (result == 0 && !reservationList.isEmpty() && !guest.reservationList.isEmpty()) {
            result = this.reservationList.stream().max(Comparator.comparing(Reservation::getCheckOut))
                    .orElseThrow(() -> new IllegalStateException("Reservation list is empty"))
                    .getCheckOut()
                    .compareTo(guest.reservationList.stream().max(Comparator.comparing(Reservation::getCheckOut))
                            .orElseThrow(() -> new IllegalStateException("Reservation list is empty"))
                            .getCheckOut());
        }
        return result;
    }
}
