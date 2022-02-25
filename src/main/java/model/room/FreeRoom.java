package model.room;

public class FreeRoom extends Room{

    //Change the constructor to set the room price to 0
    public FreeRoom() {
        super();
        price = 0.0 ;
    }

    @Override
    public String toString() {
        return "FreeRoom{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", enumeration=" + enumeration +
                '}';
    }
}
