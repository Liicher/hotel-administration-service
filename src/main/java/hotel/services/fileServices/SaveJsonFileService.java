package hotel.services.fileServices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hotel.config.ConfigValue;
import hotel.services.hotelServices.FacilityService;
import hotel.services.hotelServices.GuestService;
import hotel.services.hotelServices.ReservationService;
import hotel.services.hotelServices.RoomService;
import hotel.sort.ReservationSortParams;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaveJsonFileService {
    private ObjectMapper objectMapper;
    private final RoomService roomService;
    private final GuestService guestService;
    private final FacilityService facilityService;
    private final ReservationService reservationService;

    @ConfigValue
    private String jsonFilePath;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public void saveJsonData() {
        try {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("rooms", roomService.getAllSimple());
            dataMap.put("guests", guestService.getAllSimple());
            dataMap.put("facilities", facilityService.getAllSimple());
            dataMap.put("reservations", reservationService.getAll(new ReservationSortParams()));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), dataMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}