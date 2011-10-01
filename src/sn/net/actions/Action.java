package sn.net.actions;

import org.jboss.netty.channel.Channel;

public abstract class Action {

    // --- Constants & Variables -----------------------------------------------
    
    /**
     * Separator between fields.
     */
    public final static String MESSAGE_FIELD_SEPARATOR = " ";
    
    /**
     * Max number of fields in a command.
     */
    public final static int MESSAGE_FIELDS_MAX = 0;

    // --- Constructors --------------------------------------------------------
    
    public Action() {
    }

    public Action(String[] fields) {
    }

    // --- Getter & Setter -----------------------------------------------------
    
    // --- Public functions ----------------------------------------------------
    
    public static Action parseFromString(String data, Channel channel) {
        System.out.println("Splitting <" + data + ">");
        String[] fields = data.split(Action.MESSAGE_FIELD_SEPARATOR, Action.MESSAGE_FIELDS_MAX);

        ActionsEnum actionFound = null;
        Action actionToReturn = null;

        // FIXME: Better way?!
        // Cannot use switch with strings.. needs Java 7
        for (ActionsEnum a : ActionsEnum.values()) {
            System.out.println("Searching <" + a.toString() + "> against <" + fields[0] + ">");

            // Check first field.
            // Oracle recommends a ternary check.
            if (fields[0] == null ? a.toString() == null : fields[0].equals(a.toString())) {
                actionFound = a;
            }
        }

        // In case of unrecognized command..
        if (actionFound == null) {
            return null;
        }

        switch (actionFound) {
            case CHAT_CLOSE:
                break;
            case CHAT_OPEN:
                break;
            case CHAT_WITH:
                break;
            case CHG_STATUS:
                break;
            case DISCONNECT:
                break;
            case LIST:
                break;
            case LOGIN:
                ActionLogin.login(fields, data, channel);
                break;
        }

        return actionToReturn;
    }
    
    // --- Protected functions -------------------------------------------------
    
    // --- Private functions ---------------------------------------------------
}
