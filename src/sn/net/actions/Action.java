package sn.net.actions;

import org.jboss.netty.channel.Channel;

public abstract class Action {

    // --- Costanti & Variabili private -----------------------------------------------
    
    /**
     * Separator between fields.
     */
    public final static String MESSAGE_FIELD_SEPARATOR = " ";
    
    /**
     * Max number of fields in a command.
     */
    public final static int MESSAGE_FIELDS_MAX = 0;

    // --- Costruttori --------------------------------------------------------
    
    public Action() {
    }

    public Action(String[] fields) {
    }

    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    public static Action parseFromString(String data, Channel channel) {
        System.out.println("Splitting <" + data + ">");
        String[] fields = data.split(Action.MESSAGE_FIELD_SEPARATOR, Action.MESSAGE_FIELDS_MAX);

        Action actionToReturn = null;
        
        
        /* E' davvero fondamentale?
         * 
        // FIXME: Better way?!
        // Cannot use switch with strings.. needs Java 7
        
        // cambiato l'enum da string a int
        for (ActionsEnum a : ActionsEnum.values()) {
            System.out.println("Searching <" + a.ordinal() + "> against <" + fields[0] + ">");

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
        */
       //int a = Integer.parseInt(fields[0]);
        switch (1) {
        /*    case CHAT_CLOSE:
                break;
            case CHAT_OPEN:
                break;
            case CHAT_WITH:
                break;
            case CHG_STATUS:
                break;*/
            case ActionConnect.MESSAGE_ID:
            	ActionConnect.connect(fields, data, channel);
                break;
        /*    case DISCONNECT:
                break;
            case LIST:
                break;
            case LOGIN:
                break;*/
            case ActionLoginChatKey.MESSAGE_ID:
                ActionLoginChatKey.login(fields, data, channel);
                break;
        }

        return actionToReturn;
    }
    
    // --- Metodi protected ----------------------------------------------------
    
    // --- Metodi private ------------------------------------------------------
}
