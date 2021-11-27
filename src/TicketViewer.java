import java.util.*;

public class TicketViewer {

    private Map<Integer, Ticket> tickets;

    public TicketViewer(Map<Integer, Ticket> list) {
        tickets = list;
        System.out.println("Welcome to the ticket viewer");
        System.out.println("Type 'menu' to view options or 'quit' to exit");
    }

    public void printMenu() {
        System.out.println("View Options");
        System.out.println("1. View all tickets");
        System.out.println("2. View a ticket");
        System.out.println("Type 'quit' tp exit");
    }

    private void print25Tickets(List<Ticket> ticketList, int start) {
        for (int i = start; i < start+25 && i < tickets.size(); i++) {
            System.out.println(ticketList.get(i).toString());
        }
    }

    public void printAllTickets() {
        int listPos = 0;
        Scanner scanner = new Scanner(System.in);
        List<Ticket> ticketList = new ArrayList<>(tickets.values());
        print25Tickets(ticketList, listPos);
        while (true) {
            if (listPos == 0) {
                System.out.println("Type 'next' to show the next page or 'quit' to exit");
            }
            else if (listPos + 25 >= tickets.size()) {
                System.out.println("Type 'prev' to show the previous page or 'quit' to exit");
            }
            else {
                System.out.println("Type 'next' to show the next page, 'prev' to show the previous page, or 'quit' to exit");
            }
            String input = scanner.nextLine();
            if (input.equals("quit")) {
                System.out.println("Type 'menu' to view options or 'quit' to exit");
                return;
            }
            else if (input.equals("next")) {
                listPos += 25;
                print25Tickets(ticketList, listPos);
            }
            else if (input.equals("prev")) {
                listPos -= 25;
                print25Tickets(ticketList, listPos);
            }
            else {
                System.out.println("Invalid input");
            }
        }
    }

    public void printTicket() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type ticket id");
        int id = sc.nextInt();
        do {
            try {
                System.out.println("Type ticket id");
                id = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
            }
            sc.nextLine();
        }
        while (!tickets.containsKey(id));
        System.out.println(tickets.get(id).detailed_string());
    }
}
