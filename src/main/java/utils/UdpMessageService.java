package utils;

import org.ice4j.TransportAddress;
import org.ice4j.ice.CandidatePair;
import org.ice4j.socket.IceSocketWrapper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public class UdpMessageService {
    public static final int MESSAGE_SIZE = 1024;
    public static final int OFFSET_SIZE = 0;
    // MESSAGE_SIZE + OFFSET_SIZE must be greater than MESSAGE_BYTES.length

    public static void sendMessage(CandidatePair rtpPair, byte[] message){

        // We use IceSocketWrapper, but you can just use the UDP socket
        // The advantage is that you can change the protocol from UDP to TCP easily
        // Currently only UDP exists so you might not need to use the wrapper.

        IceSocketWrapper wrapper = rtpPair.getIceSocketWrapper();
        TransportAddress ta = rtpPair.getRemoteCandidate().getTransportAddress();
        InetAddress hostname = ta.getAddress();
        int port = ta.getPort();

        byte[] MESSAGE_BYTES = new byte[MESSAGE_SIZE + OFFSET_SIZE];
        System.arraycopy(message, 0, MESSAGE_BYTES, 0, message.length);
        DatagramPacket packet = new DatagramPacket(MESSAGE_BYTES, OFFSET_SIZE, MESSAGE_SIZE);
        packet.setAddress(hostname);
        packet.setPort(port);
        try {
            wrapper.send(packet);
        } catch (IOException e) {
            System.out.println("Error sending packet : " + e.getMessage());
        }

    }

    public static byte[] receiveMessage(CandidatePair rtpPair) {

        IceSocketWrapper wrapper = rtpPair.getIceSocketWrapper();
        DatagramPacket rpacket = new DatagramPacket(new byte[MESSAGE_SIZE + OFFSET_SIZE], OFFSET_SIZE, MESSAGE_SIZE);
        try {
            wrapper.receive(rpacket);
        } catch (IOException e) {
            System.out.println("Error receiving packet : " + e.getMessage());
        }

        //this is just done temporarily to make the output easily readable.
        //todo : this trimming of trailing zeros will be removed later
        return trim(rpacket.getData());
    }

    //to trim the trailing zeros in the byte array
    static byte[] trim(byte[] bytes)
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0)
            --i;
        return Arrays.copyOf(bytes, i + 1);
    }

}
