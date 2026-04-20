package Commands.Commands;

import Model.User;
import Model.StandardUserRegistration;
import org.hibernate.annotations.DialectOverride;

public class RegisterUserCommand implements Command{

    private String name, email, location, password;
    private StandardUserRegistration registrationLogic = new StandardUserRegistration();

    public RegisterUserCommand(String name, String email, String location, String password) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.password = password;
    }

    @Override
    public void execute(){
        registrationLogic.register(name, password, email, location);
    }

    @Override
    public User getUser() { return null; }

    @Override
    public CommandType getCommandType() {
        return null;
    }


}
