import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Ticket {
    private String subject;
    private String description;
    private long assignee_id;
    private long requester_id;
    private int id;
    private Date created_at;

    public Ticket(JSONObject json) throws JSONException {
        subject = json.getString("subject");
        description = json.getString("description");
        String date_string  = json.getString("created_at");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            created_at = sdf.parse(date_string);
        } catch (ParseException e) {
            System.out.println("Invalid Date");
            e.printStackTrace();
        }
        assignee_id = json.getLong("assignee_id");
        requester_id = json.getLong("requester_id");
        id = json.getInt("id");
    }

    public Ticket(String subject, String description,int id, int assignee_id, int requester_id, String created_at) {
        this.subject = subject;
        this.description = description;
        this.id = id;
        this.assignee_id = assignee_id;
        this.requester_id = requester_id;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            this.created_at = sdf.parse(created_at);
        } catch (ParseException e) {
            System.out.println("Invalid Date");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket "  + id);
        sb.append(" with subject '" + subject + "'");
        sb.append(" opened by " + requester_id);
        sb.append(" on " + created_at.toString());
        return sb.toString();
    }

    public String detailed_string() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket id: " + id + "\n");
        sb.append("Subject: " + subject + "\n");
        sb.append("Requester ID: " + requester_id + "\n");
        sb.append("Assignee ID: " + assignee_id + "\n");
        sb.append("Created On: " + created_at.toString() + "\n");
        sb.append("Description:\n" + description);
        return sb.toString();
    }
}
