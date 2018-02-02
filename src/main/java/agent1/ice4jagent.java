package agent1;

import utils.JsonService;
import utils.SdpUtils;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.harvest.StunCandidateHarvester;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public class ice4jagent {

    public static void main(String[] args) throws Throwable {
        Agent agent = new Agent(); // A simple ICE Agent

/*** Setup the STUN servers: ***/
        String[] hostnames = new String[] {"jitsi.org","numb.viagenie.ca","stun.ekiga.net"};

        for(String hostname: hostnames){
            try {
                TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);

                agent.addCandidateHarvester(new StunCandidateHarvester(ta));
            } catch (Exception e) {
                System.out.println("Error in setting STUN servers");
            }
        }


/*** now, lets create a media stream ***/
        IceMediaStream stream = agent.createMediaStream("audio");
        int port = 8000; // Choose any port
        agent.createComponent(stream, Transport.UDP, port, port, port+100);


/*** lets connect ***/
        String toSend = SdpUtils.createSDPDescription(agent); //Each computer sends this information

        //for instance, we suppose storing the sdp in local json file is like storing the sdp information in a
        //server or a dht from where another agent can access it. for now, another agent will read it from local
        //json file
        JsonService.storeJson("sdp1",toSend);
        TimeUnit.SECONDS.sleep(10);     //now run iceagent2.java and wait for connection

        String remoteReceived = JsonService.readJson("sdp2");
        SdpUtils.parseSDP(agent, remoteReceived); // This will add the remote information to the agent.

        System.out.println("Agent State: "+ agent.getState());

/*** lets start the connection ***/
        agent.addStateChangeListener(new StateListener());
        // You need to listen for state change so that once connected you can then use the socket.
        agent.startConnectivityEstablishment(); // This will do all the work for you to connect
    }
}
