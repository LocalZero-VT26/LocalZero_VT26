package Model;

public abstract class UserRegistrationTemplate {

    public final void register(String name, String password, String email, String location){
        validate(email, password);
        User user = createUser(name, email, location, password);
        save(user);
    }

    protected abstract void validate(String email, String password);
    protected abstract User createUser(String name, String email, String location, String password);

    protected void save(User user){
        System.out.println("User saved" + user.getEmail());
    }
}
