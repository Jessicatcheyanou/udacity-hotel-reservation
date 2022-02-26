package menu;

import api.HotelResource;
import model.customer.CustomerEmailValidation;
import model.room.*;
import model.reservation.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainMenu {

    private static final HotelResource hotelResource = HotelResource.getSingleton();

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
       try {
           return new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());

       } catch (ParseException e) {
           System.out.println("Error:Invalid Date.");
           findAndReserveRoom();
       }
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
        final CustomerEmailValidation customerEmailValidation = new CustomerEmailValidation();

        if ("y".equals(bookRoom)){
            System.out.println("Do you have an account with us?y/n");
            final String haveAccount = scanner.nextLine();
            if ("y".equals(haveAccount)){
                System.out.println("Enter your email address(following this format..name@domain.com)");
                final String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomer(customerEmail) == null || !customerEmailValidation.isValidEmail(customerEmail)){
                    System.out.println("Either InCorrect Email\n or \n Customer Not Found.\n You may need to create a new Account\n");

                }else {
                    System.out.println("What room number would you like to reserve?");
                    final String roomNumber = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))){
                        final IRoom room = hotelResource.getRoom(roomNumber);

                        final Reservation reservation = hotelResource
                                .bookARoom(customerEmail,room,checkInDate,checkOutDate);
                        System.out.println("Reservation created successfully!");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: room Number not available.\n Start reservation again");
                    }
                }
                printMainMenu();
                System.out.println("Enter 3 to Create an Account with Us and proceed.");
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
       //Get Current Date
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.format(currentDate);

        System.out.println("Enter Check-In Date (yyyy-MM-dd)");
        Date checKIn = getInputDate(scanner);

        System.out.println("Enter Check-Out Date (yyyy-MM-dd)");
        Date checKOut = getInputDate(scanner);

        try {

            if (checKIn != null && checKOut != null && (checKOut.after(checKIn) || checKOut.equals(checKIn))){
                System.out.println("Date I want to Check In:\n" + checKIn);
                System.out.println("\nDate I want to CheckOut:\n" + checKOut);
                Collection<IRoom> availableRooms = hotelResource.findAvailableRooms(checKIn,checKOut);


                if (availableRooms.isEmpty()){
                    System.out.println("\nIt seems No Rooms are available for the above dates.\n Check out alternative rooms.\n");
                    Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checKIn,checKOut);
                    alternativeRooms.forEach(System.out::println);

                    if (alternativeRooms.isEmpty()){
                        System.out.println("No Other Rooms found");
                    } else {
                        final Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checKIn);
                        final Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checKOut);
                        System.out.println("We`ve only found rooms on the following alternative dates:" +
                                "\n Check-In Date:" + alternativeCheckIn +
                                "\n Check-Out Date: " + alternativeCheckOut);

                        printRooms(alternativeRooms);
                        reserveRoom(scanner,checKIn,checKOut,alternativeRooms);
                    }
                } else{
                    System.out.println("\nList of All Available Rooms for the above dates.");
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
           reservations.forEach(reservation -> System.out.println("\n" + reservation));
       }
    }

    private static void seeMyReservations(){
       final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your email(format: name@domain.com)");
        final String customerEmail = scanner.nextLine();

        printReservations(hotelResource.getCustomersReservation(customerEmail));
    }

    private static void createAccount(){
       final Scanner scanner = new Scanner(System.in);
       final CustomerEmailValidation customerEmailValidation = new CustomerEmailValidation();

        System.out.println("Enter first Name:");
        final String firstName = scanner.nextLine();

        System.out.println("Enter Last Name:");
        final String lastName = scanner.nextLine();

        System.out.println("Enter email (format:name@domain.com):");
        final String email = scanner.nextLine();

               if (customerEmailValidation.isValidEmail(email)){
                   hotelResource.createACustomer(firstName,lastName,email);
                   printMainMenu();
                   System.out.println("Account created Successfully!");
                   System.out.println("To proceed,Enter any of the menu options Above!");
               } else{
                   System.out.println("\nError:Invalid Email.\nRestart the Account Creation process again.\nEnter an email in the following format (..name@domain.com)");
                   createAccount();
               }

    }
}

