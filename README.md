# Collaborative Graphical Editor

 - Matt Roth and Andrw Yang 


# Testing

## Echo Server
![enter image description here](https://imgur.com/EYnftjw.gif)
We used `EchoServer` to test our string commands/parsing. 

## Multiclient

![enter image description here](https://imgur.com/IDYN5ag.gif)
The GIF above shows the graphical capabilities of the editor 


# Synchronization and Multi Client
![enter image description here](https://imgur.com/PpFELtQ.gif)
## Multi Client Issues 
 - Editing the same area at approximately the same time 
	 - Because our canvas is not region or object protected, a client may edit something and block the visibility of another client's work
 - Increased lag with a lot of clients and objects (boundary case)
	 - We used a treemap as it keeps things in order. However, a tree map has run time `log(n)`. This can be a problem with many objects and clients (especially with latency) as the server iteratively proccesses the action of each clients on a large list of objects. By using a Hashmap we would have constant runtime. Reducing some latency. 
 - Editing the same shape at the exact same time
	 - If two clients edit the same shape at the same time, it may cause the server to throw a null pointer exception due to the object being gone but still there for one of the clients 
- Latency 
	- Having multiple clients means you preferrably want the canvas to be in sync for all clients. Latency is an issue as you might be drawing 1-2 seconds behind your peers. 
 - Upgrade/Changing code 
	 - With multiple clients, changes to the code (especially network protocol) must be handled carefully. Updating code for one client is easy but with many, your code must be backwards compatible. 
	
These errors were hard to reproduce as the testing was done on a local environment. In addition, some of these issues require incredibly specific timing and precision. 

##  Synchronization and `synchronize` 

We use `synchronize`'d methods to ensure that multi-threaded (multi-client) processes are consistent with each other. There are several instances where we used this. The first is in `Sketch.java` this way the server is only performing a single action when a client modifies shapes in the sketch and in the treemap. Actions are kept consistent even in this multithreaded environment. `SketchServer.java` also has synchronized methods so the server is able to process things properly. If the server did not have synchronized methods, the server would throw `Null Pointer Exception` if two clients edit at the same time. This is due to Thread collisions. 

