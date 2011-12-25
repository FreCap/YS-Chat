//thrift  --gen cpp --gen java VoiceToText.thrift

namespace cpp ThriftServer
namespace java sn.thrift
namespace php ThriftServer
namespace perl ThriftServer

service ClientVoiceToText {

	oneway void info ( 1:i32 server_id, 2:i32 max_clients, 3:string DNS, 4:i32 port_TS, 5:i32 port_Thrift )
	
}
