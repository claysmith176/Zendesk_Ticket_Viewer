import java.util.*;

public class Main {

    public static void main(String[] args) {
        RetrieveTickets retriever = new RetrieveTickets();
        Map<Integer, Ticket> tickets = retriever.getTickets();

        if (retriever.getStatus() != 200) {
            System.out.println("Error connecting to api status code " + retriever.getStatus());
            return;
        }
        if (tickets.size() == 0) {
            System.out.println("No tickets found");
            return;
        }
        TicketViewer tv = new TicketViewer(tickets);
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if (input.equals("menu")) {
                tv.printMenu();
            }
            else if (input.equals("1")) {
                tv.printAllTickets();
            }
            else if (input.equals("2")) {
                tv.printTicket();
            }
            else if (input.equals("quit")) {
                System.out.println("Goodbye");
                break;
            }
            else {
                System.out.println("Invalid input, type 'menu' to see options");
            }
        }
    }
}