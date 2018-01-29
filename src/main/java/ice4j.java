import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.harvest.StunCandidateHarvester;

import java.net.InetAddress;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public class ice4j {

    public static void main(String[] args){
        Agent agent = new Agent(); // A simple ICE Agent

/*** Setup the STUN servers: ***/
        String[] hostnames = new String[] {"jitsi.org","numb.viagenie.ca","stun.ekiga.net"};
// Look online for actively working public STUN Servers. You can find free servers.
// Now add these URLS as Stun Servers with standard 3478 port for STUN servrs.
        for(String hostname: hostnames){
            try {
                // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
                TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);
                // Currently Ice4J only supports UDP and will throw an Error otherwise
                agent.addCandidateHarvester(new StunCandidateHarvester(ta));
            } catch (Exception e) { e.printStackTrace();}
        }
    }
}
