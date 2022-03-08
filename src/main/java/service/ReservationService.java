package service;

import model.room.*;
import model.reservation.*;
import model.customer.*;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {

    private static final ReservationService SINGLETON = new ReservationService();
    public final Map<String,IRoom> rooms = new HashMap<>();

    public final Collection<Reservation> allReservations = new LinkedList<>();



    private ReservationService() {
    }

    public static ReservationService getSingleton(){
        return SINGLETON;
    }

    public void addRoom(final IRoom room){

        rooms.put(room.getRoomNumber(),room);
    }

    public IRoom getARoom(final String roomId) {
       return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms(){
        return rooms.values();
    }

    public Reservation reserveARoom(final Customer customer,final Date checkInDate,final Date checkOutDate,final IRoom room){
        final Reservation reserveTheRoom = new Reservation(customer,room,checkInDate,checkOutDate);
        boolean isRoomFree;
        isRoomFree = reserveTheRoom.getRoom().isFree();

        if (isRoomFree){
            allReservations.add(reserveTheRoom);
            isRoomFree = false;
        } else {
            System.out.println("Room already reserved!");
        }

        if (allReservations.isEmpty()){
            System.out.println("Couldn`t Add the Reservation.Try again");
        }
            return reserveTheRoom;

    }

    private Collection<Reservation> getAllReservations(){
        final Collection<Reservation> reservationsCompilation = new LinkedList<>();

        for (Reservation reservation:allReservations){
            reservationsCompilation.add(reservation);
        }

        return reservationsCompilation;
    }

    private boolean reservationOverlaps(final Reservation reservation,final Date checkInDate,final Date checkOutDate){
        return checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(checkInDate) || checkInDate.equals(reservation.getCheckOutDate());
    }


    private Collection<IRoom> findAvailableRooms(final Date checkInDate,final Date checkOutDate){
        final Collection<Reservation> allReservations = getAllReservations();
        final Collection<IRoom> notAvailableRooms = new LinkedList<>();

        for (Reservation reservation: allReservations){
             if (reservationOverlaps(reservation,checkInDate,checkOutDate)){
                 notAvailableRooms.add(reservation.getRoom());
             }
        }
        return rooms.values().stream().filter(room -> notAvailableRooms.stream()
                .noneMatch(notAvailableRoom -> notAvailableRoom.equals(room)))
                .collect(Collectors.toList());
    }

    public Collection<IRoom> findRooms(Date checkInDate,Date checkOutDate){
              return findAvailableRooms(checkInDate,checkOutDate);
    }

    public Date addDefaultPlus7Days(final Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE,7);

        return calendar.getTime();
    }

    public Date addDefaultPlusDays(final Date date,int additionalNumberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE,additionalNumberOfDays);

        return calendar.getTime();
    }

    public Collection<IRoom> findAlternativeRooms(final Date checkInDate,final Date checkOutDate){
        return findAvailableRooms(addDefaultPlus7Days(checkInDate),addDefaultPlus7Days(checkOutDate));
    }

    public Collection<Reservation> getCustomersReservation(Customer customer){
        Collection<Reservation> customersReservation = new LinkedList<>();
        for (Reservation reservation:allReservations){
            if (customer.getEmail().equals(reservation.getCustomer().getEmail())){
                customersReservation.add(reservation);
            }
        }
        return customersReservation;
    }

    public void printAllReservations(){
          final Collection<Reservation> clientsReservation = new LinkedList<>();

          clientsReservation.addAll(allReservations);

        if (clientsReservation.isEmpty()){
            System.out.println("No reservations found");
        } else {
                System.out.println("List of All Reservations");
            clientsReservation.forEach(System.out::println);
        }
    }

    //default access modifier methods
    String messageToReserva(){
        return "To reserve,You can also send a message via this email:jessicatcheyanou@gmail.com";
    }


}
