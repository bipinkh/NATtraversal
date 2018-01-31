package agent1;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetAddress;

import utils.UdpMessageService;
import org.ice4j.ice.Agent;
import org.ice4j.ice.CandidatePair;
import org.ice4j.ice.Component;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;

public class StateListener implements PropertyChangeListener {

    private InetAddress hostname;
    int port;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof Agent) {
            Agent agent = (Agent) evt.getSource();

            System.out.println("Agent state changed to " + agent.getState());

            if (agent.getState().equals(IceProcessingState.TERMINATED)) {
                // Your agent is connected. Terminated means ready to communicate
                System.out.println("Agent Connected");
                for (IceMediaStream stream : agent.getStreams()) {
                    if (stream.getName().contains("audio")) {
                        Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
                        CandidatePair rtpPair = rtpComponent.getSelectedPair();

                        /*** lets send a packet now ***/
                        UdpMessageService.sendMessage(rtpPair, "hello agent2, can you listen me ?".getBytes());

                        /*** lets wait for the reply ***/
                        byte[] mm = UdpMessageService.receiveMessage(rtpPair);
                        System.out.println("agent2 says :: " + new String(mm));
                    }
                }
            }
        }
    }
}