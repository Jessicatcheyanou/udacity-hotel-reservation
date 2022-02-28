package menu;

import api.AdminResource;
import api.HotelResource;
import model.room.*;
import model.reservation.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getSingleton();
    private static final AdminResource adminResource = AdminResource.getSingleton();

   public static void printMainMenu(){
       System.out.print("""

               Welcome to Jessica`s Hotel Reservation Application
               ____________________
               1. Find and reserve a room
               2. See my Reservations
               3. Create an account
               4. Admin
               5. Exit
               _____________________
               Please select(above) a number for your menu option of choice:
               """);
   }
   public static void mainMenu(){
       String line = "";
       Scanner scanner = new Scanner(System.in);

       printMainMenu();

       try {
           do {
               line = scanner.nextLine();

               if (line.length() == 1){
                   switch (line.charAt(0)) {
                       case '1' -> findAndReserveRoom();
                       case '2' -> seeMyReservations();
                       case '3' -> createAccount();
                       case '4' -> AdminMenu.adminMenu();
                       case '5' -> {
                           System.out.println("Exit");
                           printMainMenu();
                       }
                       default -> System.out.println("Unknown action\n");
                   }
               } else {
                   System.out.println("Error:Invalid action\n");
               }
           } while (line.charAt(0) != '5' || line.length() != '1');
       } catch (Exception ex){
           System.out.println("Empty input received.Exiting program");
       }
   }

    private static Date getInputDate(final Scanner scanner){
       boolean isValidDates = false;
       Date validDate;

       do {
           try {
               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
               validDate = simpleDateFormat.parse(scanner.nextLine());
               isValidDates = true;
               return validDate;


           } catch (ParseException e) {
               System.out.println("Error.Invalid date");
               System.out.println("Enter a Date in the following format (dd/MM/yyyy)");
           }
       } while (!isValidDates);

        return null;
    }

    private static void printRooms(final Collection<IRoom> rooms){
       if (rooms.isEmpty()){
           System.out.println("No rooms found");
       } else {
           rooms.forEach(System.out::println);
       }
    }

    private static void reserveRoom(final Scanner scanner,final Date checkInDate,final Date checkOutDate,final Collection<IRoom> rooms){
        System.out.println("Would you like to book a room with Us? y/n");
        final String bookRoom = scanner.nextLine();
        String stringRoomNumber = "";
        String stringCustomerEmail = "";
        boolean isValidEmailAndPresentCustomer = false;
        boolean isValidAndPresentRoomNumber = false;

        if ("y".equals(bookRoom)){
            System.out.println("Do you have an account with us?y/n");
            final String haveAccount = scanner.nextLine();
            if ("y".equals(haveAccount)){
                System.out.println("Let`s Check this out!");

                do {
                    try {

                        System.out.println("Enter your email address(following this format..name@domain.com)");
                        final String customerEmail = scanner.nextLine();
                        if (hotelResource.getCustomer(customerEmail) != null){
                            stringCustomerEmail  = customerEmail;
                            isValidEmailAndPresentCustomer = true;
                        }else{
                            System.out.println("Enter a Valid Email.\nOR\nCustomer Not found.\nCreate an Account to reserve a room.");
                        }
                    } catch (Exception e){
                        System.out.println("The System may be under Maintenance.Logout and come back later.");
                    }
                }while (!isValidEmailAndPresentCustomer);

                //Validate RoomNumber
                do {
                     try {
                         System.out.println("What room number would you like to reserve?");
                         final String roomNumber = scanner.nextLine();

                         if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))){
                             stringRoomNumber = roomNumber;
                             isValidAndPresentRoomNumber = true;
                         } else {
                             System.out.println("Room Number not available.\n Choose one available Room.");
                         }

                    } catch (Exception e){
                        System.out.println("The System may be under maintenance.Logout and come back later.");
                    }
                } while (!isValidAndPresentRoomNumber);

                final IRoom room = hotelResource.getRoom(stringRoomNumber);

                final Reservation reservation = hotelResource
                        .bookARoom(stringCustomerEmail,room,checkInDate,checkOutDate);
                System.out.println("Reservation created successfully!");
                System.out.println(reservation);
                printMainMenu();

            } else {
                System.out.println("Please,create an account.");
                printMainMenu();
                System.out.println("Enter 3 to Create an Account with Us and proceed.");
            }
        } else if ("n".equals(bookRoom)){
            printMainMenu();
            System.out.println("Byeee!Don`t hesitate to Enter 1,if you change your mind.");
        } else {
            reserveRoom(scanner,checkInDate,checkOutDate,rooms);
        }
    }


    private static void findAndReserveRoom(){
       final Scanner scanner = new Scanner(System.in);
       boolean isValidAddedDays = false;
       int addedDays = 0;
       String stringAddedDays;

        System.out.println("Enter Check-In Date (dd/MM/yyyy)");
        Date checKIn = getInputDate(scanner);

        System.out.println("Enter Check-Out Date (dd/MM/yyyy)");
        Date checKOut = getInputDate(scanner);

        try {

            if (checKIn != null && checKOut != null && (checKOut.after(checKIn) || checKOut.equals(checKIn))){
                System.out.println("Date I want to CheckIn:\n" + checKIn);
                System.out.println("\nDate I want to CheckOut:\n" + checKOut);
                Collection<IRoom> availableRooms = hotelResource.findAvailableRooms(checKIn,checKOut);


                if (availableRooms.isEmpty()){
                    System.out.println("\nIt seems No Rooms are available for the above dates.\n Check out alternative rooms.\n");
                    Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checKIn,checKOut);

                    if (alternativeRooms.isEmpty()){
                        System.out.println("No Other Rooms found");
                    } else {

                        final Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checKIn);
                        final Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checKOut);
                        System.out.println("We recommend the following room(s) for the following possible Dates:");
                        printRooms(alternativeRooms);
                        System.out.println("" +
                                "Possible Check-In Date:" + alternativeCheckIn +
                                "\nPossible Check-Out Date:" + alternativeCheckOut);

                        reserveRoom(scanner,checKIn,checKOut,alternativeRooms);
                    }
                } else{
                    System.out.println("\nList of Available Rooms for the above dates.");
                    printRooms(availableRooms);
                    reserveRoom(scanner,checKIn,checKOut,availableRooms);
                }

            }
        } catch (Exception e) {
            System.out.println("Please enter a Date and make sure your CheckOut Date is after or equals to your CheckIn Date.");
            findAndReserveRoom();
            e.printStackTrace();

        }

    }

    private static void printReservations(final Collection<Reservation> reservations){
       if (reservations == null || reservations.isEmpty()){
           System.out.println("No reservations Found");
       } else {
           System.out.println("List of All Reservations:");
           reservations.forEach(System.out::println);
       }
    }

    private static void seeMyReservations(){
        final Scanner scanner = new Scanner(System.in);
        boolean isValidEmail = false;

        do {
            try {
                System.out.println("Enter your email(format: name@domain.com)");
                final String customerEmail = scanner.nextLine();
                if (hotelResource.getCustomer(customerEmail) != null ){
                    printReservations(hotelResource.getCustomersReservation(customerEmail));
                    isValidEmail = true;
                }
            } catch (Exception e){
                System.out.println("Invalid Email\n.Enter a valid Email.");
            }
        } while (!isValidEmail);

    }

    private static void createAccount(){
       final Scanner scanner = new Scanner(System.in);
       boolean isValidEmail = false;
       String validEmail = "";

        System.out.println("Enter first Name:");
        final String firstName = scanner.nextLine();

        System.out.println("Enter Last Name:");
        final String lastName = scanner.nextLine();

        do {
            try {
                System.out.println("Enter email (format:name@domain.com):");
                final String email = scanner.nextLine();

                if (adminResource.getAllCustomers().stream().noneMatch(e -> e.isValidEmail(email))){
                    validEmail = email;
                    hotelResource.createACustomer(firstName,lastName,validEmail);
                    isValidEmail = true;
                    System.out.println("Account Created With Success!");
                }
            } catch (Exception e){
                System.out.println("InValid Email..\nEnter a Valid Email.");
            }
        }while (!isValidEmail);

    }
}

