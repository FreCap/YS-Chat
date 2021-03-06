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
            case ActionDisconnect.MESSAGE_ID:
            	ActionDisconnect.disconnect(obj, data, client);
                break;
            case ActionChat_With.MESSAGE_ID:
            	ActionChat_With.chat_with(obj, data, client);
                break;
            case ActionChat_Open.MESSAGE_ID:
            	ActionChat_Open.chat_open(obj, data, client);
                break;
            case ActionChat_Close.MESSAGE_ID:
            	ActionChat_Close.chat_close(obj, data, client);
                break;
            case ActionChat_AddFriend.MESSAGE_ID:
            	ActionChat_AddFriend.chat_addfriend(obj, data, client);
                break;
                /*    case CHAT_LEAVE:
                break;*/    
            case ActionChat_NoActive.MESSAGE_ID:
            	ActionChat_NoActive.chat_noactive(obj, data, client);
                break;
            /*    case CLIENT_STATUS:
                break;*/
            case ActionList.MESSAGE_ID:
                ActionList.list(obj, data, client);
                break;
                
            // VOCALE SECTION
            case ActionCall_Ring.MESSAGE_ID:
                ActionCall_Ring.call_ring(obj, data, client);
                break;
            case ActionCall_Support.MESSAGE_ID:
            	ActionCall_Support.call_support(obj, data, client);
                break;
            case ActionCall_Accept.MESSAGE_ID:
                ActionCall_Accept.call_accept(obj, data, client);
                break;
                /*    case CALL_HANGUP:
                break;*/
              
                
                
        }
 
        return null;
    }
    
    // --- Metodi protected ----------------------------------------------------
    
    // --- Metodi private ------------------------------------------------------
}
