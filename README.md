# GCOM middleware
GCOM is a java based group chat application that allows creating chat groups for sending asynchronous messages on a local network. The messages are multicasted through the group. And it also supports group leader status, that is responsible for keeping the group notified about any changes in the group and pinging the master server (RMI Server). 

And if the leader disconnects, a new leader will be automatically selected. 
### Prerequisites
```
maven 3.3.9+
Java JDK 1.8+
```
### Installing

Run the build.sh file

## Deployment
First deploy the RMI Server on the dedicated server machine, by executing "RMIServer.jar" 
Then start up the client(Client.jar), connect to the master server by its IP. 

## License 

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
