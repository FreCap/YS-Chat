package sn.net.actions;

/**
 * Elenco degli id dei comandi della chat.
 */
@Deprecated
public enum ActionsEnum {

	CONNECT (1),
	LOGIN (2),
	LOGINCHATKEY (3),
	CHANGE_STATUS (4),
	DISCONNECT (5),
	CHAT_WITH (6),
	CHAT_OPEN (7),
	CHAT_CLOSE (8),
	LIST (9);
	
    public final int ID;
    
    ActionsEnum(int ID) {
        this.ID = ID;
    }
    public int ID()   { return ID; }


}
