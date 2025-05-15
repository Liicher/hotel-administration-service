package ui.actions.reservationActions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import hotel.entities.Reservation;
import hotel.services.hotelServices.ReservationService;
import ui.actions.ActionUtils;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service("Show total price of the reservation")
@AllArgsConstructor
public class ShowTotalPriceAction implements ReservationAction {
    private ActionUtils actionUtils;
    private ReservationService reservationService;

    @Override
    public void execute() {
        try {
            List<Reservation> currentReservations = reservationService.getListOfCurrentReservations();
            currentReservations.forEach(System.out::println);
            Long reservationId = actionUtils.idInput();
            BigDecimal totalPrice = reservationService.getTotalPrice(reservationId);
            System.out.println("Total price - " + totalPrice);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
