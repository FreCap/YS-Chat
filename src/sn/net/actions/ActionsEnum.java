package sn.net.actions;

/*
 * 
Client to Server:
CONNECT (1), // { op:1 }
LOGIN (2), // { op:2, user:"String", pass:"String" }
LOGINCHATKEY (3), // { op:3, profilo_id:int, chat_key:"String" }
CHG_STATUS (4), // { op:4, status:int }
DISCONNECT (5), // { op:5 }
CHAT_WITH (6), // { op:6, profilo_id:int, message:"String" }
CHAT_OPEN (7), // { op:7, profilo_id:int }
CHAT_CLOSE (8), //  { op:8, profilo_id:int }
CHAT_NOACTIVE (9), // { op:9 }
CLIENT_STATUS (10), // { op:10, status:IDLED|ONLINE ( int }
LIST (11); // { op:11 }

Server to Client:
CONNECT (1), // { op:1, salt:"String" }
LOGIN (2), // { op:2, result:Bool }
LOGINCHATKEY (3), // { op:3, result:Bool }
CHG_STATUS (4), // { op:4, status:int }
DISCONNECT (5), // { op:5 }
CHAT_WITH (6), // { op:6, actived:int, messages: [{me:int, profilo_id:int, message:"String"},{...}] }
CHAT_OPEN (7), // { op:7, chatTab_active:int, chatTab:[{profilo_id:int, order:int}]}
CHAT_CLOSE (8), //  { op:8, profilo_id:int }
CHAT_NOACTIVE (9), // { op:9 }
LIST (11); // { op:11, list:[{profilo_id:int, nickname:String},{profilo_id:int, nickname:String},{profilo_id:int, nickname:String}] }

 */

@Deprecated
public enum ActionsEnum {
	CONNECT (1),
	LOGIN (2),
	LOGINCHATKEY (3),
	CHG_STATUS (4),
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
