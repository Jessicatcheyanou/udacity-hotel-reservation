package model.room;

import model.room.RoomType;

public interface IRoom {
     String getRoomNumber();
     Double getRoomPrice();
     RoomType getRoomType();
     boolean isFree();

}
