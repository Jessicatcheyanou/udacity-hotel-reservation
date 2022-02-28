package menu;

import api.AdminResource;
import model.customer.Customer;
import model.room.IRoom;
import model.room.Room;
import model.room.RoomType;

import java.util.*;
import java.util.stream.Stream;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getSingleton();

    private static void printMenu(){
        System.out.println("""

                Admin Menu
                ___________________________________
                1. See all Customers
                2. See all Rooms
                3. See all Reservations
                4. Add a Room
                5. Add Test Data(Customers,Rooms and Reservations)
                6. Back to Main Menu
                ____________________________________
                Please select(above) a number for your menu option of choice:
                """);
    }
   public static void adminMenu(){
       String line = "";
       final Scanner scanner = new Scanner(System.in);

       printMenu();

       try {
           do {
               line = scanner.nextLine();
               if (line.length() == 1){
                   switch (line.charAt(0)) {
                       case '1' -> displayAllCustomers();
                       case '2' -> displayAllRooms();
                       case '3' -> displayAllReservations();
                       case '4' -> addRoom();
                       case '5' -> loadCustomersRoomsAndReservationsTestData();
                       case '6' -> MainMenu.mainMenu();
                       default -> System.out.println("Unknown action\n");
                   }
               } else {
                   System.out.println("Error: Invalid action\n");
               }
           } while (line.charAt(0) != '5' || line.length()!=1);
       } catch (Exception exception){
           System.out.println("Exiting program.");
       }

   }
   private static double enterRoomPrice(final Scanner scanner){
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exp){
            System.out.println("Invalid room price!Please,enter a valid double number." +
                    "Decimals should be separated by point(.)");
            return enterRoomPrice(scanner);
        }
   }

   private static RoomType enterRoomType(final Scanner scanner){
        try {
            return RoomType.valueOf(scanner.nextLine());
        } catch (IllegalArgumentException exp){
            System.out.println("Invalid room type!Please,enter either SINGLE or DOUBLE");
             return enterRoomType(scanner);
        }
   }

   private static void addAnotherRoom(){
        final Scanner scanner = new Scanner(System.in);

        try {
            String anotherRoom;

            anotherRoom = scanner.nextLine();

            while ((anotherRoom.charAt(0) != 'Y' && anotherRoom.charAt(0) != 'N') || anotherRoom.length() != 1){
                System.out.println("Please enter Y (Yes) or N (No)");
                anotherRoom = scanner.nextLine();
            }
            if (anotherRoom.charAt(0) == 'Y'){
                addRoom();
            } else if (anotherRoom.charAt(0) == 'N'){
                printMenu();
            } else {
                addAnotherRoom();
            }
        } catch (Exception exception){
            addAnotherRoom();
        }
   }

   private static void displayAllRooms(){
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if (rooms.isEmpty()){
            System.out.println("No rooms found.");
        } else {
            System.out.println("List of All Rooms:");
            rooms.forEach(System.out::println);
        }
   }

   private static void displayAllCustomers(){
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()){
            System.out.println("No Customers found");
        } else {
            System.out.println("List of All Customers");
            customers.forEach(System.out::println);
        }

   }

   private static void displayAllReservations(){
        adminResource.displayAllReservations();
   }

   private static void addRoom(){
        final Scanner scanner = new Scanner(System.in);
        boolean isValidCustomerEmail = false;

        //Check Admin/Customer privileges to add or not a Room
        do {
            try {
                System.out.println("Enter your Email:");
                final String email = scanner.nextLine();
                if(adminResource.getAllCustomers().stream().anyMatch(e -> e == adminResource.getCustomer(email))){
                    isValidCustomerEmail = true;
                }else {
                    System.out.println("Either Your Email is incorrect\n OR \nYou are not a Customer nor Admin in our Hotel.\n Create an Account to be Able to Add a Room");
                }
            } catch (Exception e){
                System.out.println("You are not a Customer nor Admin in our Hotel.\n Create an Account to be Able to Add a Room");
            }
        }while (!isValidCustomerEmail);

        System.out.println("Enter room number");
        final String roomNumber = scanner.nextLine();

        System.out.println("Enter price per night:");
        final double roomPrice = enterRoomPrice(scanner);

       System.out.println("Enter either : SINGLE or DOUBLE as Room Type");
       final RoomType roomType = enterRoomType(scanner);

       final Room room = new Room(roomNumber,roomPrice,roomType);

       adminResource.addRoom(Collections.singletonList(room));
       System.out.println("Room added successfully!");

       System.out.println("Would like to add another room?Y/N");
       addAnotherRoom();
   }

   public static void loadCustomersRoomsAndReservationsTestData() throws Exception{

        System.out.println("\nList of Existing Customers:");
        adminResource.loadCustomersTestData();

        System.out.println("\nList of Existing Rooms:");
        adminResource.loadRoomsTestData();

        System.out.println("\nList of Existing Reservations:");
        adminResource.loadReservationsTestData();

        System.out.println("\nCustomers,Rooms and Reservations Test Data Loaded With Success!");
        MainMenu.printMainMenu();
   }

}
