

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static Properties get_properties() throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("/config.properties"));
        return properties;
    }

    public static void get_tickets(Properties prop) throws IOException, InterruptedException {
        URI uri = URI.create("https://" + prop.getProperty("subdomain") +".zendesk.com/api/v2/tickets.json");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email") + "/token", prop.getProperty("api_token").toCharArray());
            }
        };
        HttpClient client = HttpClient.newBuilder().authenticator(auth).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void print_menu() {
        System.out.println("View Options");
        System.out.println("1. View all tickets");
        System.out.println("2. View a ticket");
        System.out.println("Type 'quit' tp exit");
    }

    public static void main(String[] args) {
        Properties prop = null;
        try {
            prop = get_properties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            get_tickets(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Welcome to the ticket viewer");
        System.out.println("Type 'menu' to view options or 'quit' to exit");
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if (input.equals("menu")) {
                print_menu();
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
