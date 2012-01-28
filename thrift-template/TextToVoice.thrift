//thrift  --gen cpp --gen java TextToVoice.thrift

namespace cpp ThriftServer
namespace java sn.thrift
namespace php ThriftServer
namespace perl ThriftServer

service ClientTextToVoice {

	bool new_channel ( 1:i32 server_id, 2:string nome, 3:string password )
	bool add_client ( 1:i32 server_id, 2:string channel_name, 3:string client_name )

}
