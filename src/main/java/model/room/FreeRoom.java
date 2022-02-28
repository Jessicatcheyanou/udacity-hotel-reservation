package model.room;

public class FreeRoom extends Room{
    //Change the constructor to set the room price to 0
    public FreeRoom (String roomNumber, Double price, RoomType enumeration) {
        super(roomNumber, 0.0 , enumeration);
    }

}
