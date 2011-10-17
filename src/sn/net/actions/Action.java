package sn.net.actions;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ibdknox.socket_io_netty.INSIOClient;

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
    
    public static Action parseFromString(String data, INSIOClient client) {
        
    	JSONObject obj = (JSONObject) JSONValue.parse(data);
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
    	
    	int op = Integer.parseInt(Long.toString((Long) obj.get("op")));
        switch (op) {
        /*    case CHAT_CLOSE:
                break;
            case CHAT_OPEN:
                break;
            case CHAT_WITH:
                break;
            case CHG_STATUS:
                break;*/
            case ActionConnect.MESSAGE_ID:
            	ActionConnect.connect(obj, data, client);
                break;
        /*    case DISCONNECT:
                break;
            case LIST:
                break;
            case LOGIN:
                break;*/
            case ActionLoginChatKey.MESSAGE_ID:
                ActionLoginChatKey.login(obj, data, client);
                break;
        }
 
        return null;
    }
    
    // --- Metodi protected ----------------------------------------------------
    
    // --- Metodi private ------------------------------------------------------
}
