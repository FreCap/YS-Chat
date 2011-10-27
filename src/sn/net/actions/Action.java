package sn.net.actions;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ibdknox.socket_io_netty.INSIOClient;


public abstract class Action {

    // --- Costanti & Variabili private -----------------------------------------------
    
    // --- Costruttori --------------------------------------------------------
    
    public Action() {
    }

    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    public static Action parseFromString(String data, INSIOClient client) {
        
    	JSONObject obj = (JSONObject) JSONValue.parse(data);
        
    	int op = Integer.parseInt(Long.toString((Long) obj.get("op")));
        switch (op) {
        	case ActionConnect.MESSAGE_ID:
        		ActionConnect.connect(obj, data, client);
        		break;
        	/* case LOGIN:
                break;*/
            case ActionLoginChatKey.MESSAGE_ID:
                ActionLoginChatKey.login(obj, data, client);
                break;
            /*     case CHG_STATUS:
                    break;*/
            /*    case DISCONNECT:
                    break;*/
            case ActionChat_With.MESSAGE_ID:
            	ActionChat_With.chat_with(obj, data, client);
                break;
            case ActionChat_Open.MESSAGE_ID:
            	ActionChat_Open.chat_open(obj, data, client);
                break;
            case ActionChat_Close.MESSAGE_ID:
            	ActionChat_Close.chat_close(obj, data, client);
                break;
            case ActionChat_NoActive.MESSAGE_ID:
            	ActionChat_NoActive.chat_noactive(obj, data, client);
                break;
            /*    case CLIENT_STATUS:
                break;*/
            case ActionList.MESSAGE_ID:
                ActionList.list(obj, data, client);
                break;
        }
 
        return null;
    }
    
    // --- Metodi protected ----------------------------------------------------
    
    // --- Metodi private ------------------------------------------------------
}
