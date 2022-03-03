package model.reservation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.ReservationService;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ReadAndSaveReservation {
    private final ReservationService reservationService = ReservationService.getSingleton();

    public void readAndSaveReservation() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/reservation.json");
        Collection<Reservation> storeMyReservations = new LinkedList<>();

        List<Reservation> jsonMap = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        for (Reservation reservation:jsonMap){
            if(reservationService.allReservations.add(reservation)){
                storeMyReservations = reservationService.allReservations;
            }
        }
        System.out.println(storeMyReservations);
    }

}
