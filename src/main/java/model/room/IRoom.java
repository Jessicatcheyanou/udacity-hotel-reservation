package model.room;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import model.room.RoomType;

@JsonDeserialize(as = Room.class)
public interface IRoom {
     String getRoomNumber();
     Double getRoomPrice();
     RoomType getRoomType();
     boolean isFree();

}
