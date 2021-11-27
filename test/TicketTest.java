import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {
    @Test
    public void testJSONConstructor() throws JSONException {
        Ticket testTicket = new Ticket("subject", "description", 1, 23, 24,"2021-11-20T16:44:51Z");
        JSONObject testJSON = new JSONObject();
        testJSON.put("subject", "subject");
        testJSON.put("description", "description");
        testJSON.put("created_at", "2021-11-20T16:44:51Z");
        testJSON.put("assignee_id", 23);
        testJSON.put("requester_id", 24);
        testJSON.put("id", 1);
        Ticket JSONTicket = new Ticket(testJSON);
        assertEquals(JSONTicket.toString(), testTicket.toString());
        assertEquals(JSONTicket.detailed_string(), testTicket.detailed_string());
    }

    @Test
    public void testString() {
        Ticket testTicket = new Ticket("subject", "description", 1, 23, 24,"2021-11-20T16:44:51Z");
        //assertEquals("");
        String simple = "Ticket 1 with subject 'subject' opened by 24 on Sat Nov 20 16:44:51 EST 2021";
        String detailed = "Ticket id: 1\nSubject: subject\nRequester ID: 24\nAssignee ID: 23\nCreated On: Sat Nov 20 16:44:51 EST 2021\nDescription:\ndescription\n";
        assertEquals(simple, testTicket.toString());
        assertEquals(detailed, testTicket.detailed_string());
    }
}
