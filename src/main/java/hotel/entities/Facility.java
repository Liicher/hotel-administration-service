package hotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "facility")
public class Facility implements Comparable<Facility> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facility_name", nullable = false)
    private String facilityName;

    @Column(name = "facility_price", nullable = false)
    private BigDecimal facilityPrice;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "facilityList")
    @BatchSize(size = 10)
    private List<Reservation> reservationList = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Override
    public int compareTo(Facility o) {
        return this.facilityPrice.compareTo(o.facilityPrice);
    }
}
