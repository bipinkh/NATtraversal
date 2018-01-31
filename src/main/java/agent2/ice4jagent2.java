package agent2;

import utils.JsonService;
import utils.SdpUtils;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.harvest.StunCandidateHarvester;

import java.net.InetAddress;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public class ice4jagent2 {


    public static void main(String[] args) throws Throwable {
        Agent agent = new Agent();

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
        int port = 8100;
        agent.createComponent(stream, Transport.UDP, port, port, port+100);


/*** lets connect ***/
        String toSend = SdpUtils.createSDPDescription(agent);
        JsonService.storeJson("sdp2",toSend);
        String remoteReceived = JsonService.readJson("sdp1");
        SdpUtils.parseSDP(agent, remoteReceived);

        System.out.println("Agent2 State: "+ agent.getState());

/*** lets start the connection ***/
        agent.addStateChangeListener(new StateListener2());
        agent.startConnectivityEstablishment();
    }
}
