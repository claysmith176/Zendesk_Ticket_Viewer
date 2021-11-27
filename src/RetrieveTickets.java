import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RetrieveTickets {
    private Map<Integer, Ticket> tickets;

    /**
     * Loads the config properties file
     * @return A Properties object containing the properties
     * @throws IOException when unable to read config file
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            System.out.println("Unable to read config file");
            e.printStackTrace();
        }
        return properties;
    }

    public RetrieveTickets() {
        tickets = new HashMap<>();
        Properties prop = getProperties();
        URI uri = URI.create("https://" + prop.getProperty("subdomain") +".zendesk.com/api/v2/tickets.json");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email") + "/token", prop.getProperty("api_token").toCharArray());
            }
        };
        JSONArray tickets_json = new JSONArray();
        try {
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
            for (int i = 0; i < tickets_json.length(); i++) {
                int id = tickets_json.getJSONObject(i).getInt("id");
                Ticket curr_ticket = new Ticket(tickets_json.getJSONObject(i));
                tickets.put(id, curr_ticket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Unable to parse JSON data");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("Invalid URI from server");
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the API
     * @param auth the authenticator to use
     * @param uri the uri to send the request to
     * @return the response from the API
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> send_request(Authenticator auth, URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient client = HttpClient.newBuilder().authenticator(auth).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public Map<Integer, Ticket> getTickets() {
        return tickets;
    }
}
