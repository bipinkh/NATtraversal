# NATtraversal
code snippets for NAT traversal.

## guideline for running the code

1.) import project as a maven project (preferably in IntelliJIdea

2.) run iceagent.main()

3.) After receiving message 

"Successfully updated json object to file...!!" 

run iceagent2.main()

--> The iceagent and iceagent2 now acts like two different agents communicating to each other.

--> Data Send, Receive and different protocols for data transfer can be changed in the StateListener.

--> Different port assign for streaming can be changed in main() of respective agent.

## note
for instance, we are storing the sdp information of each agent in local json file. In actual implementation, it is stored in a server or a dht from where another agent can access it.

***

reference - [here](http://www.stellarbuild.com/blog/article/ice4j-networking-tutorial-part-1)
