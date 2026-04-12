package Commands;

import Model.User;
import Model.Initiative;

public class CreateInitiativeCommand implements Command {
    private String title;
    private String description;
    private String location;
    private Manager manager = new Manager();
    private User user;







    public CreateInitiativeCommand(User user, String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.user = user;
    }

    public void execute()
    {
        System.out.println("Create initiative Command");
        Initiative initiative = new Initiative(title, description, location);
        sendInitiative(initiative);

    }

    public void sendInitiative(Initiative initiative)
    {
        System.out.println("Initiative has been sent to the system");
        manager.addInitiative(initiative);
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public CommandType getCommandType() {
        return  CommandType.CREATE_INITIATIVE;
    }
}
