/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function log(variabile){
				$(".Console").prepend(variabile);
		$(".Console").prepend("<br/>");
		console.log(variabile);
	}

var Chat = function(start){
	
	log("Started");
	
	var this2 = this;

	var statuses = { 
							DISCONNECTED: 0,
							CONNECTING: 1,
							CONNECTED: 2
					};// TODO da aggiungerne
   
	var actions = { 
							CONNECT : 1, // { op:1 }
							LOGIN : 2, // { op:2, user:"String", pass:"String" }
							LOGINCHATKEY : 3, // { op:3, profilo_id:int, chat_key:"String" }
							CHG_STATUS : 4, // { op:4, status:int }
							DISCONNECT : 5, // { op:5 }
							CHAT_WITH : 6, // { op:6, profilo_id:int, message:"String" }
							CHAT_OPEN : 7, // { op:7, profilo_id:int }
							CHAT_CLOSE : 8, //  { op:8, profilo_id:int }
							CHAT_ADDFRIEND: 9, // { op:9, profilo_id:(int), conv_id:(int) }
							CHAT_LEAVE: 10, // { op:10, conv_id:(int) }
							CHAT_NOACTIVE : 11, // { op:9 }
							CLIENT_STATUS : 12, // { op:10, status:0(IDLED)|1(ONLINE) ( int }
							LIST : 13, // { op:13, tipo: FRIENDS|CHAT_OPENED|ALL (int) }

							CALL_RING: 21, // { op:21, conv_id:(int) }
							CALL_SUPPORT: 22, // { op:22 }
							CALL_ACCEPT: 23, // { op:23, conv_id:(int), support:(boolean) }

							CALL_WAIT: 24, // FromServerOnly { op:23, conv_id:(int), call_id:"String"}
							CALL: 25, // FromServerOnly { op:24, conv_id:(int), call_id:"String", call_password:"String", call_version:(int), server:(int), port:(int) }
							CALL_HANGUP: 26 // { op:26, conv_id;(int), call_id:"String" }
						};

	var lists_tipo = {
							FRIENDS: 1,
							CHAT_OPENED: 2,
							ALL: 3
					};

	var voicePluginName = "NAME";// TODO

	this.friends_online = [];
	this.chatTab_opened = [];
	this.chatTab_active = 0;
	this.status = 0;

	this.profilo_id = 0;

	this.messages_toWrite = [];

	this.has_voicePlugin = function(){
			
		//return true;//TODO DEBUG
		
		var isInstalled = false;
		var version = null;
		if (window.ActiveXObject) {
		  var control = null;
		  try {
				control = new ActiveXObjectvoicePluginName();
		  } catch (e) {
				return;
		  }
		  if (control) {
				return true;
		  }
		} else {

				$.each(navigator.plugins, function(i, l){
						if(l.name == voicePluginName){
								return true;
						}
				});
		}

		return false;
	};

	this.get_loginInfo = function(){
					var loginMethod = $("#loginMethod").val();
					var parametro;
					if (loginMethod == actions.LOGINCHATKEY){
							//DEBUG
							$("#profilo_id").val((Math.floor(Math.random()*4)+1));//6
							//$("#profilo_id").val(3);

							document.title = $("#profilo_id").val();
							this2.profilo_id = parseInt($("#profilo_id").val());
							parametro = { 
											op: actions.LOGINCHATKEY,
											profilo_id: parseInt($("#profilo_id").val()),
											chat_key: $("#chat_key").val()								
									};
					}else{
							parametro = { 
											op: actions.LOGIN
											/* TODO, ricordarsi che si logga con l'account poi si deve scegliere il profilo */
									}	;
					}
					return parametro;
			},

	this.server = {
			Login: function(){
					message = { op: actions.CONNECT };
					this2.sendSocket(message);
			},
			List: function(tipo){
					if(tipo){

					}else{
							tipo = lists_tipo.ALL;
					}
					var parametro = {
													op: actions.LIST,
													tipo: tipo
											};

					this2.sendSocket(parametro);
			},
			Chat_open: function(profilo_id_open){
					var parametro = {
													op: actions.CHAT_OPEN,
													profilo_id: parseInt(profilo_id_open)
											};
					this2.sendSocket(parametro);
			},
			Chat_close: function(profilo_id_close){
					var parametro = {
													op: actions.CHAT_CLOSE,
													profilo_id: parseInt(profilo_id_close)
											};
					this2.sendSocket(parametro);
			},
			Chat_addFriend: function(conv_id, profilo_id){
					var parametro = {
											op: actions.CHAT_ADDFRIEND,
											profilo_id: parseInt(profilo_id),
											conv_id: parseInt(conv_id)
									};
					this2.sendSocket(parametro);
			},
			Chat_noActive: function(){
					var parametro = {
													op: actions.CHAT_NOACTIVE,
											};
					this2.sendSocket(parametro);
			},
			Chat_with: function(profilo_idSend, messageSend){
						var parametro = {
												op: actions.CHAT_WITH,
												profilo_id: parseInt(profilo_idSend),
												message: messageSend
											};
					this2.sendSocket(parametro);
			},
			Call_ring: function(conv_idCall){
					var parametro = {
													op: actions.CALL_RING,
													conv_id: parseInt(conv_idCall)
											};
					this2.sendSocket(parametro);
			},
			Call_support: function(){
					var parametro = {
													op: actions.CALL_SUPPORT,
													support : this2.has_voicePlugin()
											};
					this2.sendSocket(parametro);
			},
			Call_accept: function(conv_idCall, support){
					var parametro = {
													op: actions.CALL_ACCEPT,
													conv_id: parseInt(conv_idCall),
													support: support
											};
					this2.sendSocket(parametro);
			},
			Call_hangup: function (conv_idCall){
				  var parametro = {
						  op: actions.CALL_HANGUP,
						  conv_id: parseInt(conv_idCall),
				  };
				  this2.sendSocket(parametro);
			}
	};

	this.GUI = {
			chat_opened_container: $(".OpenedChatWrapper").find(".second"),
			profilo_link_model: jQuery('<a/>').addClass('clearfix profilo').attr('href','#').append(jQuery('<a/>')),
			profilo_model: function(profilo_id,nickname){
					var profilo_link_model = this2.GUI.profilo_link_model.clone();
					profilo_link_model.attr("profilo_id", profilo_id);
					profilo_link_model.find("a").text(nickname);
					return profilo_link_model;
			},
			update_List: function(){
					$(".profiliList").empty();
					$.each(this2.friends_online, function(i, l){
							this2.GUI.profilo_model(l.profilo_id, l.nickname).appendTo($(".profiliList"));
					});

					//TODO to remove, debug
					this2.GUI.initEvents();
			},
			initEvents: function(){
					$(".profiliList").find(".profilo").livequery("click", function(e){
							this2.GUI.chatTab_Manager.open($(this).attr("profilo_id"));
					});
			},
			chatTab_Manager:{
					open: function(profilo_id, server){
							this2.GUI.chatTab_Manager.create(profilo_id);
							this2.GUI.chat_opened_container.find(".ChatTab.opened").removeClass("opened");
							this2.GUI.chat_opened_container.find(".ChatTab#" + profilo_id).addClass("opened");
							if(server != true){
									this2.server.Chat_open(profilo_id);
							}
					},
					create: function(profilo_id, server){
							var already_opened = false;
							$.each(this2.chatTab_opened, function(i, l){
									if(l == profilo_id){
											already_opened = true;
											return true;
									}
							});
							if(!already_opened){
									var Chat_tab = $("#itemChat").find(".ChatTab").clone();
									var profilo;
									var found = false;
									//TODO puramente per test, in realtà nell'originale è da usare il datamanager
									$.each(this2.friends_online, function(i, l){
											if(profilo_id == l.profilo_id){
													profilo = l;
													found = true;
											}
									});
									if(!found){
											this2.server.List(lists_tipo.FRIENDS);
											return false;
									}else{
											//fine TOD
											Chat_tab.attr("id",profilo.profilo_id);
											Chat_tab.find(".nickname").text(profilo.nickname);
											this2.chatTab_opened.push(profilo_id);
											this2.chatTab_active = profilo_id;
											this2.GUI.chat_opened_container.append(Chat_tab);
											this2.GUI.chatTab_Manager.initEvents(Chat_tab);
											return true;
									}
							}
							return true;
					},
					close: function(profilo_id, server){
							$.each(this2.chatTab_opened, function(i, l){
									if(l == profilo_id){
											this2.chatTab_opened.splice(i,1);
									}
							});
							if(profilo_id == this2.chatTab_active){
									this2.chatTab_active = 0;
							}
							this2.GUI.chat_opened_container.find(".ChatTab#" + profilo_id).remove();
							if(server != true){
									this2.server.Chat_close(profilo_id);
							}

					},
					reduce: function(profilo_id, server){
							this2.chatTab_active = 0;
							this2.GUI.chat_opened_container.find(".ChatTab#" + profilo_id).removeClass("opened");
							if(server != true){
									this2.server.Chat_noActive();
							}
					},
					write: function(conv_id, profilo_id, message, server, messageJson){
							//TODO
							if(this2.GUI.chatTab_Manager.create(conv_id, server)){
									this2.GUI.chat_opened_container.find(".ChatTab#" + conv_id).find(".Body")
													.append(profilo_id + ": " + message)
													.append("</br>");
									if(server != true){
											this2.server.Chat_with(conv_id, message);
									}
							}else{
									this2.messages_toWrite.push(messageJson);
							}
					},
					initEvents: function(tab){
							tab = this2.GUI.chat_opened_container.find(".ChatTab#"+tab.attr("id"));
							tab.find(".ButtonOpen").bind("click", function(e){
									this2.GUI.chatTab_Manager.open(tab.attr("id"));
							});
							tab.find(".close").bind("click", function(e){
									this2.GUI.chatTab_Manager.close(tab.attr("id"));
									e.stopPropagation();
							});
							tab.find(".addPartecipante").bind("click", function(e){

									//TODO schedina di account etc..
									profilo_id = 1;// infatti questa è di debug

									this2.server.Chat_addFriend(tab.attr("id"), profilo_id);
									e.stopPropagation();
							});
							tab.find(".call").bind("click", function(e){

								/* TODO
								 * check se supporta la call
								 * check se ci sono chiamate già attive -> scelta se interromperla
								 * call_ring DONE
								 * call_wait
								 */

								this2.server.Call_ring(tab.attr("id"));
								e.stopPropagation();
							});
							tab.find(".Header").bind("click", function(e){
									this2.GUI.chatTab_Manager.reduce(tab.attr("id"));
							});

							tab.find("#Input").bind("change", function(e){
									this2.GUI.chatTab_Manager.write(tab.attr("id"),this2.profilo_id , $(this).val(), false);
									$(this).val("");
							});
					}
			}
	};

	this.calls = {



	};

	this.socket = new io.Socket("192.168.1.4", {//127.0.0.1
	  port: 9999,
	  transports: [/*'websocket','flashsocket',*/ 'xhr-polling'],
	  rememberTransport: false
	});

	this.start = function(){
			this2.socket.connect();
	};

	this.socket.on('connect',function() {
	  log('Connesso!');
	  this2.server.Login();
	});

	this.socket.on('message',function(data) {
			log('<< RECEIVING: ' + data);

			var data_JSON = $.parseJSON(data);
			switch (data_JSON.op) { 
					case actions.CONNECT: 
							if(this2.status == statuses.DISCONNECTED){
									this2.status = statuses.CONNECTING;
									var message = this2.get_loginInfo();
									var salt = data_JSON.salt;
									if (message.op == actions.LOGINCHATKEY){
											// cripta la chat_key
											message.chat_key = MD5(message.chat_key+""+salt);						
									}else if(message.op == actions.LOGIN){
											//TODO
									}
									this2.sendSocket(message);
							}
							break; 
					case actions.LOGIN: 
					case actions.LOGINCHATKEY: 
							if(data_JSON.result == true){
									this2.status = statuses.CONNECTED;
									this2.server.List();
									this2.server.Call_support();
							}else{
									//TODO vari casi di errore, implementazione server mancante
							}
							break;
					case actions.CHG_STATUS:
							break;
					case actions.DISCONNECT:
							break;
					case actions.CHAT_WITH:
							$.each(data_JSON.messages, function(i, l){
									if(l.message){
											this2.GUI.chatTab_Manager.write(l.conv_id, l.profilo_id, l.message, true, l);
									}else if(l.messages){
											$.each(l.messages, function(i2, l2){

													this2.GUI.chatTab_Manager.write(l.conv_id, l2.s, l2.m, true, l);
											});
									}else{
											log("error");
									}
							});
							break;
					case actions.CHAT_OPEN:						
							$.each(data_JSON.chatTab, function(i, l){
									this2.GUI.chatTab_Manager.create(l.profilo_id, true);
							});
							if(data_JSON.chatTab_active != 0){
									this2.GUI.chatTab_Manager.open(data_JSON.chatTab_active, true);
							}
							break;
					case actions.CHAT_CLOSE:
							this2.GUI.chatTab_Manager.close(data_JSON.profilo_id, true);
							break;
					case actions.CHAT_ADDFRIEND:

							break;
					case actions.CHAT_LEAVE:	
							break;	
					case actions.CHAT_NOACTIVE:
							this2.GUI.chatTab_Manager.reduce(this2.chatTab_active, true);
							break;
					case actions.LIST:
							this2.friends_online = data_JSON.list;
							this2.GUI.update_List();
							$.each(this2.messages_toWrite, function(i, l){
									this2.GUI.chatTab_Manager.write(l.conv_id, l.profilo_id, l.message, true, l);
									this2.messages_toWrite.splice(i,1);
							});
							break;
					case actions.CALL_RING:
							
							break;
					case actions.CALL_SUPPORT:
						if(data_JSON.support == false){
							if(data_JSON.conv_id == this2.profilo_id){
								
								alert("TU non supporti la chat vocale")
							}else{
								alert("l'account " + data_JSON.conv_id + " non supporta la chat vocale");							
							}
						}else{
							//TODO è possibile che il supporto sia true?? verifica
							
						}
						break;
					case actions.CALL_WAIT:
						
						break;
					case actions.CALL:
						
						break;
					case actions.CALL_HANGUP:
						
						break;
			};
	});
	this.socket.on('disconnect',function() {
	  log('The client has disconnected!');
	  this2.status = statuses.DISCONNECTED;
	});

	this.sendSocket = function(message){
			log(">> SENDING:" + JSON.stringify(message));
			this2.socket.send(JSON.stringify(message));
	};

	if(start == true){
			this.start();
	};

};

$(document).ready(function(){

	

	test = new Chat(true);
	

});
var test;