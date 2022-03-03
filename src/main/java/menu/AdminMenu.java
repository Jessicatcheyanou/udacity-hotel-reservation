package menu;

import api.AdminResource;
import model.customer.Customer;
import model.room.IRoom;
import model.room.Room;
import model.room.RoomType;

import java.util.*;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getSingleton();

    private static void printAdminMenu(){
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
       String stringMenuChoice;
       int menuChoice = 0;
       final Scanner scanner = new Scanner(System.in);

       printAdminMenu();

       try {
           do {
               stringMenuChoice = scanner.nextLine();
               menuChoice = Integer.parseInt(stringMenuChoice);
               if (stringMenuChoice.length() == 1){
                   switch (menuChoice) {
                       case 1 -> showAllCustomers();
                       case 2 -> showAllRooms();
                       case 3 -> showAllReservations();
                       case 4 -> addARoom();
                       case 5 -> loadCustomersRoomsAndReservationsTestData();
                       case 6 -> MainMenu.mainMenu();
                       default -> System.out.println("Choose amongst the above actions\n");
                   }
               } else {
                   System.out.println("Error: Invalid input\n");
               }
           } while (menuChoice != 6);
       } catch (Exception exception){
           System.out.println("Leaving program.");
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
        String stringAddAnotherRoom;
        char charAddAnotherRoom;
        boolean isAdded = false;
        boolean isNotAdded = false;

        do {
            try {
                stringAddAnotherRoom = scanner.nextLine();
                charAddAnotherRoom = stringAddAnotherRoom.charAt(0);

                if (charAddAnotherRoom == 'Y'){
                    addARoom();
                    isAdded = true;
                }else {
                    printAdminMenu();
                    //set to TRUE TO 2exit the loop
                    isAdded = true;
                }
            }catch (Exception e){
                System.out.println("We might be facing some maintenance issues.Please do wait,we will get back to you.");
            }

        }while (!isAdded);

   }

   private static void showAllRooms(){
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if (rooms.isEmpty()){
            System.out.println("No rooms found.");
        } else {
            System.out.println("List of All Rooms:");
            rooms.forEach(System.out::println);
        }
   }

   private static void showAllCustomers(){
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()){
            System.out.println("No Customers found");
        } else {
            System.out.println("List of All Customers");
            customers.forEach(System.out::println);
        }

   }

   private static void showAllReservations(){
        adminResource.displayAllReservations();
   }

   private static void addARoom(){
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

       System.out.println("Would you like to add another room?Y/N");
       addAnotherRoom();
   }

   private static void loadCustomersRoomsAndReservationsTestData() throws Exception{

        System.out.println("\nList of Existing Customers:");
        adminResource.loadCustomersTestData();

        System.out.println("\nList of Existing Rooms:");
        adminResource.loadRoomsTestData();

        System.out.println("\nList of Existing Reservations:");
        adminResource.loadReservationsTestData();

        System.out.println("\nCustomers,Rooms and Reservations Test Data Loaded With Success!");
   }

}
