

import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    // loads the config.properties file
    public static Properties get_properties() throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("/config.properties"));
        return properties;
    }

    public static HttpResponse<String> send_request(Authenticator auth, URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient client = HttpClient.newBuilder().authenticator(auth).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public static Map<Integer, Ticket> get_tickets(Properties prop) throws IOException, InterruptedException, JSONException, URISyntaxException {
        URI uri = URI.create("https://" + prop.getProperty("subdomain") +".zendesk.com/api/v2/tickets.json");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email") + "/token", prop.getProperty("api_token").toCharArray());
            }
        };
        JSONArray tickets_json = new JSONArray();
        while (true) {
            HttpResponse<String> response = send_request(auth, uri);
            JSONObject curr_json = new JSONObject(response.body());
            for (int i = 0; i < curr_json.getJSONArray("tickets").length(); i++) {
                tickets_json.put(curr_json.getJSONArray("tickets").getJSONObject(i));
            }
            if (curr_json.isNull("next_page")) {
                break;
            }
            uri = new URI(curr_json.getString("next_page"));
        }
        Map<Integer, Ticket> tickets = new HashMap<>();
        for (int i = 0; i < tickets_json.length(); i++) {
            System.out.println(i);
            String subject = tickets_json.getJSONObject(i).getString("subject");
            String description = tickets_json.getJSONObject(i).getString("description");
            String created_at = tickets_json.getJSONObject(i).getString("created_at");
            int assignee_id = tickets_json.getJSONObject(i).getInt("assignee_id");
            int requester_id = tickets_json.getJSONObject(i).getInt("requester_id");
            String status = tickets_json.getJSONObject(i).getString("status");
            int id = tickets_json.getJSONObject(i).getInt("id");
            JSONArray tags = tickets_json.getJSONObject(i).getJSONArray("tags");
            ArrayList<String> tags_string = new ArrayList<String>();
            for (int j = 0; j < tags.length(); j++) {
                tags_string.add(tags.getString(j));
            }
            Ticket curr_ticket = new Ticket(subject, description, assignee_id, requester_id, created_at, status, tags_string);
            tickets.put(id, curr_ticket);
        }
        return tickets;
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
        Map<Integer, Ticket> tickets = null;
        try {
            tickets = get_tickets(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
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
