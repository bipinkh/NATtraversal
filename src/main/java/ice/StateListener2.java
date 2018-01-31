package ice;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.socket.IceSocketWrapper;

public class StateListener2 implements PropertyChangeListener {

    private InetAddress hostname;
    int port;
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() instanceof Agent){
            Agent agent = (Agent) evt.getSource();

            System.out.println("Agent state changed to " + agent.getState());

            if(agent.getState().equals(IceProcessingState.TERMINATED)) {
                // Your agent is connected. Terminated means ready to communicate
                System.out.println("Agent Connected");
                for (IceMediaStream stream: agent.getStreams()) {
                    if (stream.getName().contains("audio")) {
                        Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
                        CandidatePair rtpPair = rtpComponent.getSelectedPair();
                        // We use IceSocketWrapper, but you can just use the UDP socket
                        // The advantage is that you can change the protocol from UDP to TCP easily
                        // Currently only UDP exists so you might not need to use the wrapper.
                        IceSocketWrapper wrapper  = rtpPair.getIceSocketWrapper();
                        // Get information about remote address for packet settings
                        TransportAddress ta = rtpPair.getRemoteCandidate().getTransportAddress();
                        hostname = ta.getAddress();
                        port = ta.getPort();


/*** lets receive a packet now ***/
                        byte[] buf = new byte[256];
                        DatagramPacket rpacket = new DatagramPacket(buf, buf.length);
                        try {
                            System.out.println("waiting for data");
                            wrapper.receive(rpacket); // This will block until you receive data that you can use.
                            System.out.println("received data successful ! "+ new String (rpacket.getData()));
                        } catch (IOException e) {
                            System.out.println("Error receiving packet : "+e.getMessage());
                        }

/*** lets send the reply ***/
                        buf = "hi there".getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        packet.setAddress(hostname);
                        packet.setPort(port);
                        try {
                            wrapper.send(packet);
                            System.out.println("Sent reply successful ::  "+ new String(packet.getData()));
                        } catch (IOException e) {
                            System.out.println("Error sending packet : " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}