package Commands;

import Model.User;
import Model.Initiative;

public class CreateInitiativeCommand {
    private User user;
    private String title;
    private String description;
    private String location;




    public CreateInitiativeCommand(User user, String title, String description, String location) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public void execute()
    {
        Initiative initiative = new Initiative(title, description, location);
        System.out.println("Create initiativeCommand");
    }


    public User getUser()
    {
        return user;
    }

}
