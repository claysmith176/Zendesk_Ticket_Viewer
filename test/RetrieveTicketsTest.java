import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveTicketsTest {
    @Test
    public void test()throws IOException, InterruptedException, JSONException {
        Properties prop = new Properties();
        try {
            prop.load(Main.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            System.out.println("Unable to read config file");
            e.printStackTrace();
        }

        URI uri = URI.create("https://" + prop.getProperty("subdomain") +".zendesk.com/api/v2/tickets/count.json");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("email") + "/token", prop.getProperty("api_token").toCharArray());
            }
        };
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpClient client = HttpClient.newBuilder().authenticator(auth).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject count = new JSONObject(response.body());
        int num = count.getJSONObject("count").getInt("value");

        RetrieveTickets rt = new RetrieveTickets();
        Map<Integer, Ticket> tickets = rt.getTickets();
        assertEquals(num, tickets.size());
        assertEquals(200, rt.getStatus());
    }
}
