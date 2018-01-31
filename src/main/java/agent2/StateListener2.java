package agent2;

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

public class StateListener2 implements PropertyChangeListener {

    private InetAddress hostname;
    int port;
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource() instanceof Agent){
            Agent agent = (Agent) evt.getSource();

            System.out.println("Agent state changed to " + agent.getState());

            if(agent.getState().equals(IceProcessingState.TERMINATED)) {
                System.out.println("Agent Connected");
                for (IceMediaStream stream: agent.getStreams()) {
                    if (stream.getName().contains("audio")) {
                        Component rtpComponent = stream.getComponent(org.ice4j.ice.Component.RTP);
                        CandidatePair rtpPair = rtpComponent.getSelectedPair();

                        /*** lets listen for the packet now ***/
                        byte[] msg = UdpMessageService.receiveMessage(rtpPair);
                        System.out.println("agent1 says :: "+ new String(msg));

                        /*** lets send the reply ***/
                        UdpMessageService.sendMessage(rtpPair, "yes, i am listening".getBytes());

                    }
                }
            }
        }
    }
}