import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class PlaneManagement {
    //To define maximum seat counts for each row
    private static final int MAX_ROWA = 14;
    private static final int MAX_ROWB = 12;
    private static final int MAX_ROWC = 12;
    private static final int MAX_ROWD = 14;

    // To initialize arrays to track seat bookings for each row
    private static int[] rowA = new int[MAX_ROWA];
    private static int[] rowB = new int[MAX_ROWB];
    private static int[] rowC = new int[MAX_ROWC];
    private static int[] rowD = new int[MAX_ROWD];

    // Array to store booked tickets
    private static Ticket[]tickets = new Ticket[52];
    private static int tickets_count=0;
    public static void main (String[]args){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Welcome to the Plane Management application");
        int option; // Display menu and handle user input
        do {
            printMenu();
            try{
                option= scanner.nextInt();
                switch (option){
                    case 1 :
                        buySeat(scanner);
                        break;
                    case 2 :
                        cancelSeat(scanner);
                        break;
                    case 3 :
                        findFirstAvailable();
                        break;
                    case 4 :
                        showSeatingPlan();
                        break;
                    case 5 :
                        print_tickets_info();
                        break;
                    case 6 :
                        search_ticket(scanner);
                        break;
                    case 0 :
                        System.out.println("Quit");
                        break;
                    default:
                        System.out.println("Invalid option");

                }
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid integer 0 to 6.");
                scanner.nextLine(); //To clear the invalid input
                option=-1; //To continue the loop
            }
        }while (option!=0);
        scanner.close();
    }
    private static void printMenu() {
        System.out.println("*************************************************");
        System.out.println("*                  MENU OPTIONS                 *");
        System.out.println("*************************************************");
        System.out.println("        1) Buy a seat                                ");
        System.out.println("        2) Cancel a seat                             ");
        System.out.println("        3) Find first available seat                 ");
        System.out.println("        4) Show seating plan                         ");
        System.out.println("        5) Print tickets information and total sales ");
        System.out.println("        6) Search ticket                             ");
        System.out.println("        0) Quit                                      ");
        System.out.println("*************************************************");
        System.out.print("Please select an option: ");
    }
    public static void buySeat(Scanner scanner) {
        try {

            System.out.print("Enter row letter (A-B-C-D): ");
            char row_letter = scanner.next().toUpperCase().charAt(0);

            if (row_letter < 'A' || row_letter > 'D') {
                System.out.println("Invalid row letter. Please enter again");
                return;
            }

            System.out.print("Enter seat number: ");
            int seat_number = scanner.nextInt();

            if ((row_letter == 'A' && seat_number > 14) || (row_letter == 'B' && seat_number > 12)
                    || (row_letter == 'C' && seat_number > 12)) {
                System.out.println("Invalid seat number. Please enter again ");
                return;
            }
            int[] selectedRow;
            switch (row_letter) {
                case 'A':
                    selectedRow = rowA;
                    break;
                case 'B':
                    selectedRow = rowB;
                    break;
                case 'C':
                    selectedRow = rowC;
                    break;
                case 'D':
                    selectedRow = rowD;
                    break;
                default:
                    System.out.println("Invalid row. Please enter again");
                    return;
            }
            if (seat_number < 1 || seat_number > selectedRow.length) {
                System.out.println("Invalid seat number for this row. enter again.");
                return;
            }
            if (selectedRow[seat_number - 1] == 1) {
                selectedRow[seat_number - 1] = 0;
                System.out.println("Seat " + row_letter + seat_number + " is already booked");
                // Getting Information from user
            } else {
                System.out.println("Enter person's name:- ");
                String name = scanner.next();

                System.out.println("Enter person's surname:- ");
                String surname = scanner.next();

                System.out.println("Enter person's email:- ");
                String email = scanner.next();

                Person person = new Person(name, surname, email);
                double price = calculate_price(row_letter, seat_number);
                Ticket ticket = new Ticket(row_letter, seat_number, price, person);

                // Save ticket information to file
                ticket.save();

                // Book the seat
                selectedRow[seat_number - 1] = 1;
                tickets[tickets_count++] = ticket;// Add ticket to the tickets array

                System.out.println("Seat " + row_letter + seat_number + " booked successfully");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input detected. Please enter valid data.");
            scanner.nextLine();
        }
    }
    private static double calculatePriceForRow(char row_letter, int seat_number) {
        double price;

        // Define the price range for each row
        double lowPrice = 200.0;
        double midPrice = 150.0;
        double highPrice = 180.0;

        // Determine the price based on the seat number and row letter
        if (seat_number >= 1 && seat_number <= 14) {
            if (seat_number >= 1 && seat_number <= 5) {
                price = lowPrice;
            } else if (seat_number >= 6 && seat_number <= 9) {
                price = midPrice;
            } else {
                price = highPrice;
            }
        } else {
            price = 0.0; // Invalid seat number for any row
        }

        return price;
    }
    private static double calculate_price(char row_letter, int seat_number) {
        double price;

        switch (row_letter) {
            case 'A':
                price = calculatePriceForRow(row_letter, seat_number);
                break;
            case 'B':
            case 'C':
            case 'D':
                price = calculatePriceForRow(row_letter, seat_number);
                break;
            default:
                price = 0.0;
                break;
        }

        return price;
    }

    private static void deleteTicketFile(char row_letter, int seat_number) {
        String fileName = row_letter + Integer.toString(seat_number) + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted: " + fileName);
            } else {
                System.out.println("Failed to delete: " + fileName);
            }
        } else {
            System.out.println("File not found: " + fileName);
        }
    }
    // Method to handle canceling a seat
    private static void cancelSeat(Scanner scanner) {
        try {
            System.out.println("Enter row letter (A-B-C-D): ");
            char row_letter = scanner.next().toUpperCase().charAt(0);

            if (row_letter < 'A' || row_letter > 'D') {
                System.out.println("Invalid row letter. Please enter again.");
                return;
            }

            System.out.println("Enter seat number: ");
            int seat_number = scanner.nextInt();

            int[] selectedRow;
            switch (row_letter) {
                case 'A':
                    selectedRow = rowA;
                    break;
                case 'B':
                    selectedRow = rowB;
                    break;
                case 'C':
                    selectedRow = rowC;
                    break;
                case 'D':
                    selectedRow = rowD;
                    break;
                default:
                    System.out.println("Invalid row letter. Please enter again");
                    return;
            }

            if (seat_number < 1 || seat_number > selectedRow.length) {
                System.out.println("Invalid seat number for this row. Enter again.");
                return;
            }

            if (selectedRow[seat_number - 1] == 1) {
                selectedRow[seat_number - 1] = 0;
                System.out.println("Seat " + row_letter + seat_number + " canceled");

                // Remove the corresponding ticket and delete the ticket file
                for (int i = 0; i < tickets_count; i++) {
                    if (tickets[i].getRow() == row_letter && tickets[i].getSeat() == seat_number) {
                        deleteTicketFile(row_letter, seat_number);

                        for (int j = i; j < tickets_count - 1; j++) {
                            tickets[j] = tickets[j + 1];
                        }
                        tickets[tickets_count - 1] = null;
                        tickets_count--;
                        break;
                    }
                }

            } else {
                System.out.println("Seat " + row_letter + seat_number + " is not booked.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid data.");
            scanner.nextLine();
        }
    }
    private static void findFirstAvailable() {
        for (int[] row : new int[][] { rowA, rowB, rowC, rowD }) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] == 0) {
                    char row_letter = getRow_Letter(row);
                    int seat_number = i + 1;
                    System.out.println("First available seat: " + row_letter + seat_number);
                    return;

                }
            }
        }
        System.out.println("No available seats.");
    }
    private static char getRow_Letter(int[] row) {
        if (row == rowA) {
            return 'A';
        } else if (row == rowB) {
            return 'B';
        } else if (row == rowC) {
            return 'C';
        } else {
            return 'D';
        }
    }
    private static void showSeatingPlan() {
        System.out.println("Seating Plan: ");
        for (int[] row : new int[][] { rowA, rowB, rowC, rowD }) {
            for (int seat : row) {
                if (seat == 0) {
                    System.out.print("O");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
    // Method to print tickets information and total sales
    private static void print_tickets_info() {
        double total_amount = 0.00;////////////
        System.out.println("Tickets information: ");
        for (int l = 0; l < tickets_count; l++) {
            Ticket ticket = tickets[l];
            double ticket_price = ticket.getPrice();
            total_amount += ticket_price;///////////////

            System.out.println(
                    "Name: " + ticket.getPerson().getName() + " " + "Surname: " + ticket.getPerson().getSurname() + " "
                            + "Ticket " + ticket.getRow() + " " + ticket.getSeat() + ": price £" + ticket_price);
        }
        System.out.println("Total sales: £ " + total_amount);/////////

    }
    private static void search_ticket(Scanner scanner) {
        try {
            System.out.println("Enter row letter(A-B-C-D): ");
            char row_letter = scanner.next().toUpperCase().charAt(0);

            // Validate row letter input before proceeding
            if (row_letter < 'A' || row_letter > 'D') {
                System.out.println("Invalid row letter. Please enter again");
                return;
            }

            System.out.println("Enter seat number: ");
            int seat_number = scanner.nextInt();

            int[] selectedRow;
            switch (row_letter) {
                case 'A':
                    selectedRow = rowA;
                    break;
                case 'B':
                    selectedRow = rowB;
                    break;
                case 'C':
                    selectedRow = rowC;
                    break;
                case 'D':
                    selectedRow = rowD;
                    break;
                default:
                    System.out.println("Invalid row. Please enter again");
                    return;
            }

            // Validate seat number input to prevent ArrayIndexOutOfBoundsException
            if (seat_number < 1 || seat_number > selectedRow.length) {
                System.out.println("Invalid seat number for this row. Enter again.");
                return;
            }

            if (selectedRow[seat_number - 1] == 1) {
                System.out.println("This seat is booked.");
                for (Ticket ticket : tickets) {
                    if (ticket != null && ticket.getRow() == row_letter && ticket.getSeat() == seat_number) {
                        ticket.printTicketInformation();
                        return;
                    }
                }
            } else {
                System.out.println("This seat is available.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine();
        }
    }
}