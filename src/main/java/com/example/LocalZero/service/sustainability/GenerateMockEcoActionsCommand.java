package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.EcoAction;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.repository.EcoActionRepository;

import java.util.List;

public class GenerateMockEcoActionsCommand implements IEcoCommand {

    private final User user;
    private final EcoActionRepository repository;

    private static final List<String> MOCK_ACTIVITIES = List.of(
            "Cycled to work instead of driving",
            "Recycled plastic and glass bottles",
            "Participated in a community park clean-up",
            "Composted organic food waste",
            "Switched to energy-efficient LED bulbs",
            "Used public transport for daily commute",
            "Planted a tree in the neighborhood",
            "Reduced shower time to save water",
            "Bought locally produced vegetables",
            "Organized a zero-waste community workshop"
    );

    public GenerateMockEcoActionsCommand(User user, EcoActionRepository repository) {
        this.user = user;
        this.repository = repository;
    }

    @Override
    public void execute() {
        for (String activity : MOCK_ACTIVITIES) {
            EcoAction action = new EcoAction(activity, user);
            repository.save(action);
        }
    }
}
