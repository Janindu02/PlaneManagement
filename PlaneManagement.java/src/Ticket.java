import java.io.IOException;
import java.io.FileWriter;
// Class representing a Ticket for a flight
public class Ticket {
    private char row;
    private int seat;
    private double price;
    private Person person;

    public Ticket(char row,int seat,double price,Person person){
        this.row=row;
        this.seat=seat;
        this.price=price;
        this.person = person;
    }
    public char getRow() {
        return row;
    }

    public void setRow(char row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    // print information of a Ticket
    public void printTicketInformation(){
        System.out.println("Ticket Information:-");
        System.out.println("Row:- " + row);
        System.out.println("Seat Information:- " + seat);
        System.out.println("Price:- " + price);
        System.out.println("Person Information:- ");
        person.printPersonInformation();
    }
    // To save Ticket Information to a file
    public void save(){
        String file_name = row + Integer.toString(seat)+ ".txt";
        try {
            FileWriter writer = new FileWriter(file_name);
            writer.write("Ticket Information:-\n");
            writer.write("Row "+row+"\n");
            writer.write("Seat Information:- "+ seat+"\n");
            writer.write("Price:- " +price+"\n");
            writer.write("Person Information:- \n");
            writer.write("Name:- "+ person.getName()+ "\n");
            writer.write("Surname:- "+person.getSurname()+"\n");
            writer.write("Email:-" + person.getEmail()+"\n");
            writer.close();
            System.out.println("Ticket information saved to file:- "+ file_name);
        }catch (IOException e){
            System.out.println("An error occurred when saving ticket information to file:- "+ e.getMessage());
        }
    }
}
