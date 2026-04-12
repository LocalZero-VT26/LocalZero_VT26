package Commands.Logic;

import Model.Initiative;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private List<Initiative> initiatives;

    public Manager() {
        initiatives = new ArrayList<>();
    }

    public void addInitiative(Initiative initiative)
    {
        initiatives.add(initiative);
    }


}
