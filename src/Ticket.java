import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Ticket {
    private String subject;
    private String description;
    private int assignee_id;
    private int requester_id;
    Date created_at;
    boolean open;
    ArrayList<String> tags;

    public Ticket(String subject, String description, int assignee_id, int requester_id, String created_at, String status, ArrayList<String> tags) {
        this.subject = subject;
        this.description = description;
        this.assignee_id = assignee_id;
        this.requester_id = requester_id;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        try {
            this.created_at = sdf.parse(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.open = status.equals("open");
        this.tags = tags;
    }

}
