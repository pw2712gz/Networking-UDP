package org.ayubyusuf;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple UDP server that receives messages from clients and echoes them back.
 */
public class Server {
	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
	private final DatagramSocket datagramSocket;
	private final byte[] buffer = new byte[256];

	/**
	 * Constructs a new Server.
	 *
	 * @param datagramSocket The datagram socket for receiving and sending UDP packets.
	 */
	public Server(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
	}

	/**
	 * The main method to start the server.
	 *
	 * @param args Command line arguments (not used).
	 * @throws IOException If an I/O error occurs when creating the socket.
	 */
	public static void main(String[] args) throws IOException {
		DatagramSocket datagramSocket = new DatagramSocket(1234);
		Server server = new Server(datagramSocket);
		server.receiveThenSend();
	}

	/**
	 * Starts the server to receive messages from clients and send responses.
	 * This method runs in an infinite loop, echoing received messages back to the sender.
	 */
	public void receiveThenSend() {
		LOGGER.info("Server started. Listening for messages...");
		while (true) {
			try {
				DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(datagramPacket);

				InetAddress inetAddress = datagramPacket.getAddress();
				int port = datagramPacket.getPort();
				String messageFromClient = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				LOGGER.info("Message from client: " + messageFromClient);

				// Optionally modify the message or handle it here

				datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
				datagramSocket.send(datagramPacket);
				LOGGER.info("Echoed message back to client.");
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Exception in receiveThenSend method", e);
				break; // Exit the loop on exception
			}
		}
	}
}
