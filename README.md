# YSChat

Ad-hoc Java chat server, based on a custom protocol

It's included also the client side inside the dir "client"

## Tech

For this project has been used:

- Java as main technology
- Thrift to communicate with other server in other languages: the chat server would have 
    asked to the voice server to open a channel, to allow a vocal chat
- Netty as network layer
- socket.io as super network layer to communicate with the client
- JSON as packet encapsulator 
- Redis NoSQL for the caching of the communications (another part 
    of the server was supposed to write them down afterwards)
- MySQL to fetch the data of the accounts and group chats from the server

Client:
- Javascript
- socket.io


## License

This work is licensed under a "Creative Commons Attribution-NoDerivatives 4.0 International (CC BY-ND 4.0)" 

http://creativecommons.org/licenses/by-nd/4.0/legalcode

