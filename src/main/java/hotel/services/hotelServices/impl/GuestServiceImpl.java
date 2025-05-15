package hotel.services.hotelServices.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotel.dto.guest.request.GuestRequest;
import hotel.entities.Guest;
import hotel.entities.Reservation;
import hotel.exceptions.NotFoundException;
import hotel.mappers.GuestMapper;
import hotel.repository.interfaces.GuestRepository;
import hotel.services.hotelServices.GuestService;
import hotel.sort.GuestSortParams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Override
    @Transactional
    public void addAll(Map<Long, Guest> guests) {
        guestRepository.saveAll(guests.values());
    }

    @Override
    public List<Guest> getAll(GuestSortParams params) {
        return guestRepository.findAllSorted(params);
    }

    @Override
    public List<Guest> getAllSimple() {
        return guestRepository.findAllSimpleInfo();
    }

    @Override
    @Transactional
    public void createGuest(String name, String lastName) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setLastName(lastName);
        guestRepository.save(guest);
    }

    @Override
    @Transactional
    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    @Transactional
    public Guest update(Long id, GuestRequest request) {
        Guest existingGuest = getGuestById(id);
        guestMapper.updateEntity(existingGuest, request);
        return guestRepository.update(existingGuest);
    }

    @Override
    @Transactional
    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("%s\t-\tReservation with id - %d doesn't exist", getClass().getName(), id)));;
        guest.setIsActive(false);
        guestRepository.update(guest);
    }

    @Override
    @Transactional
    public void addReservation(Guest guest, Reservation reservation) {
        guest.addReservation(reservation);
        guestRepository.update(guest);
    }

    @Override
    @Transactional
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("%s\t-\tGuest with id - %d doesn't exist", getClass().getName(), id)));
    }

    @Override
    @Transactional
    public List<Guest> getSortedListOfAllGuests() {
        List<Guest> guests = getAllSimple();
        Collections.sort(guests);
        return guests;
    }

    @Override
    public List<Guest> getSortedListOfSettledGuests() {
        List<Guest> guests = getAllSimple();
        guests = guests.stream().filter(Guest::getIsSettled).collect(Collectors.toList());
        Collections.sort(guests);
        return guests;
    }

    @Override
    public Integer getAmountOfSettledGuests() {
        List<Guest> guests = getSortedListOfSettledGuests();
        return guests.size();
    }

    @Override
    public Reservation getCurrentReservation(Guest guest) {
        if (!guest.getIsSettled()) {
            throw new NotFoundException(String.format("%s\t-\tGuest doesn't have any reservations!", getClass().getName()));
        }
        return guest.getReservationList().getLast();
    }

    @Override
    @Transactional
    public void update(Guest guest) {
        guestRepository.update(guest);
    }

    @Override
    @Transactional
    public Long getActiveCount() {
        return guestRepository.countALl();
    }
}
