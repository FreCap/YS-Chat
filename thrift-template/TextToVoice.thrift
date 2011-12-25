//thrift  --gen cpp --gen java TextToVoice.thrift

namespace cpp ThriftServer
namespace java sn.thrift
namespace php ThriftServer
namespace perl ThriftServer

service ClientTextToVoice {

	bool new_channel ( 1:string nome, 2:string password )
	oneway void add_client ( 1:string channel_name, 2:string client_name )

}
