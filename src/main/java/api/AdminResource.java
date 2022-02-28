package api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.customer.Customer;
import model.customer.ReadAndSaveCustomer;
import model.reservation.ReadAndSaveReservation;
import model.room.IRoom;
import model.room.ReadAndSaveRoom;
import service.CustomerService;
import service.ReservationService;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class AdminResource  {

    private static final AdminResource SINGLETON = new AdminResource();
    private final CustomerService customerService = CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();


    private AdminResource() {
    }

    public static AdminResource getSingleton(){
        return SINGLETON;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    //Add a room
    public void addRoom(List<IRoom> rooms){
        rooms.forEach(reservationService::addRoom);
    }

    //Get All Rooms
    public Collection<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }

    //Get All Customers
    public Collection<Customer> getAllCustomers(){

       return customerService.getAllCustomers();
    }

    //Show all reservations
    public void displayAllReservations(){
        reservationService.printAllReservations();
    }

    //Load customers test Data
    public void loadCustomersTestData() throws Exception {
        final ReadAndSaveCustomer readAndSaveCustomer = new ReadAndSaveCustomer();
        readAndSaveCustomer.readAndSaveCustomer();
    }

    //Load Rooms Test Data
    public void loadRoomsTestData() throws Exception{
        final ReadAndSaveRoom readAndSaveRoom = new ReadAndSaveRoom();
        readAndSaveRoom.readAndSaveRooms();
    }

    //Load Reservations Test Data
    public void loadReservationsTestData() throws Exception{
        final ReadAndSaveReservation readAndSaveReservation = new ReadAndSaveReservation();
        readAndSaveReservation.readAndSaveReservation();
    }

}
