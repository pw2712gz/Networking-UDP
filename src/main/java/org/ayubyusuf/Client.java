package org.ayubyusuf;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple UDP client that sends messages to a server and receives responses.
 */
public class Client {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	private final DatagramSocket datagramSocket;
	private final InetAddress inetAddress;

	/**
	 * Constructs a new Client.
	 *
	 * @param datagramSocket The datagram socket for sending and receiving UDP packets.
	 * @param inetAddress    The IP address of the server.
	 */
	public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
		this.datagramSocket = datagramSocket;
		this.inetAddress = inetAddress;
	}

	/**
	 * Main method to run the client.
	 *
	 * @param args Command line arguments (not used).
	 * @throws SocketException      If a socket operation fails.
	 * @throws UnknownHostException If the IP address of the host could not be determined.
	 */
	public static void main(String[] args) throws SocketException, UnknownHostException {
		DatagramSocket datagramSocket = new DatagramSocket();
		InetAddress inetAddress = InetAddress.getByName("localhost");
		Client client = new Client(datagramSocket, inetAddress);
		LOGGER.info("Send messages to the server. Type your messages in the console.");
		client.sendThenReceive();
	}

	/**
	 * Sends messages to the server and prints the server's response.
	 */
	public void sendThenReceive() {
		try (Scanner scanner = new Scanner(System.in)) {
			LOGGER.info("Client started. Type messages to send to server.");
			while (true) {
				String messageToSend = scanner.nextLine();
				// Assuming message size is within 256 bytes
				byte[] buffer = messageToSend.getBytes();
				DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 1234);
				datagramSocket.send(datagramPacket);

				// Prepare packet for receiving response
				datagramPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(datagramPacket);
				String messageFromServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				LOGGER.info("The server says: " + messageFromServer);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "IOException in sendThenReceive method", e);
		}
	}
}
