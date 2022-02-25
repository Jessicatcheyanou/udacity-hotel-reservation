package model.reservation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.ReservationService;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ReadAndSaveReservation {
    private final ReservationService reservationService = ReservationService.getSingleton();

    public void readAndSaveReservation() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/reservation.json");

        List<Reservation> jsonMap = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        for (Reservation object: jsonMap){
              reservationService.addReservation(object);
            System.out.println(object);

        }
    }

    public static void main(String[] args) throws Exception {
        ReadAndSaveReservation readAndSaveReservation = new ReadAndSaveReservation();
        readAndSaveReservation.readAndSaveReservation();
    }

}
