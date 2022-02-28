package api;

import model.customer.Customer;
import model.room.*;
import model.reservation.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.*;

public class HotelResource {

     private static final HotelResource SINGLETON = new HotelResource();
     private final CustomerService customerService = CustomerService.getSingleton();
     private final ReservationService reservationService = ReservationService.getSingleton();

    private HotelResource() {
    }

    public static HotelResource getSingleton(){
        return SINGLETON;
    }

    //Get a Customer
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    //Create a Customer
    public void createACustomer(String firstName,String lastName,String email){
        customerService.addCustomer(firstName,lastName,email);
    }

    //Search and Get a room
    public IRoom getRoom(String roomNumber){
       return reservationService.getARoom(roomNumber);
    }

    //A Customer books a room
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate,Date checkOutDate){
        return reservationService.reserveARoom(getCustomer(customerEmail),checkInDate,checkOutDate,room);
    }

    //Get all reservations for a Customer using his email
    public Collection<Reservation> getCustomersReservation(String customerEmail){
        final Customer customer = getCustomer(customerEmail);

        if (customer == null){
            System.out.println("Customer doesn`t exist,thus No Reservations found for him.");
        }
        return reservationService.getCustomersReservation(getCustomer(customerEmail));
    }

    //find available rooms using checkIn and checkOut Date
    public Collection<IRoom> findAvailableRooms(final Date checkInDate,final Date checkOutDate) {
        return reservationService.findRooms(checkInDate,checkOutDate);
    }

    //Find Alternative Rooms
    public Collection<IRoom> findAlternativeRooms(final Date checkIn,final Date checkOut){
        return reservationService.findAlternativeRooms(checkIn,checkOut);
    }

    //Add Default Plus 7 Days
    public Date addDefaultPlusDays(final Date date){
        return reservationService.addDefaultPlus7Days(date);
    }

    //Add Plus Days
    public Date addPlusDays(final Date date,int addedDays){
        return reservationService.addDefaultPlusDays(date,addedDays);
    }

}
