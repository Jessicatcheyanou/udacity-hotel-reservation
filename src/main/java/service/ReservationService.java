package service;

import model.room.*;
import model.reservation.*;
import model.customer.*;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {

    private static final ReservationService SINGLETON = new ReservationService();
    private final Map<String,IRoom> rooms = new HashMap<>();

    final Collection<Reservation> allReservations = new LinkedList<>();
    private final Map<String,Collection<Reservation>> reservationsPerCustomer = new HashMap<>();


    public ReservationService() {
    }

    public static ReservationService getSingleton(){
        return SINGLETON;
    }

    public void addRoom(final IRoom room){

        rooms.put(room.getRoomNumber(),room);
    }

    public void addReservation(final Reservation reservation){
           if (allReservations.add(reservation)){
        reservationsPerCustomer.put(reservation.getCustomer().getEmail(),allReservations);
           }
    }

    public IRoom getARoom(final String roomId) {
       return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms(){
        return rooms.values();
    }

    public Reservation reserveARoom(final Customer customer,final Date checkInDate,final Date checkOutDate,final IRoom room){
        final Reservation reserveTheRoom = new Reservation(customer,room,checkInDate,checkOutDate);

        Collection<Reservation> customerReservations = getCustomersReservation(customer);

            if (customerReservations == null){
                customerReservations = new LinkedList<>();

            }

            customerReservations.add(reserveTheRoom);
            reservationsPerCustomer.put(customer.getEmail(), customerReservations);

            return reserveTheRoom;

    }

    private Collection<Reservation> getAllReservations(){

        for (Collection<Reservation> reservations: reservationsPerCustomer.values()){
            allReservations.addAll(reservations);
        }
        return allReservations;
    }

    private boolean reservationOverlaps(final Reservation reservation,final Date checkInDate,final Date checkOutDate){
        return checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate());
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

    public Collection<Reservation> getCustomersReservation(final Customer customer){
       return reservationsPerCustomer.get(customer.getEmail());
    }

    public void printAllReservations(){
        final Map<String,Collection<Reservation>> reservationsForCustomer = reservationsPerCustomer;

        if (reservationsForCustomer.isEmpty()){
            System.out.println("No reservations found");
        } else {
                System.out.println("List of All Reservations");
                reservationsForCustomer.values().forEach(System.out::println);
        }
    }


}
