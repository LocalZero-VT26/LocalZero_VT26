package Commands.Logic;

import Commands.Commands.Command;

public class PermissionHandler extends CommandHandler{
    private PermissionChecker permissionChecker;

    public PermissionHandler(PermissionChecker permissionChecker)
    {
        this.permissionChecker = permissionChecker;
    }

    @Override
    protected void process(Command command) {
        command.execute();
    }

    @Override
    protected boolean canHandle(Command command) {
        return permissionChecker.isAllowed(command.getUser(), command);
    }
}
