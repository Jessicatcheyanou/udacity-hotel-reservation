package model.room;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.ReservationService;

import java.io.InputStream;
import java.util.List;

public class ReadAndSaveRoom {
    private final ReservationService reservationService = ReservationService.getSingleton();

    public void readAndSaveRooms() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/room.json");
        List<Room> roomList = objectMapper.readValue(inputStream, new TypeReference<>() {
        });

        for (Room room:roomList){
            reservationService.addRoom(room);
            System.out.println(room);
        }


    }
}
