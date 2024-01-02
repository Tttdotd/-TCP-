package com.ouc.tcp.test;

import java.util.TimerTask;

import com.ouc.tcp.client.Client;
import com.ouc.tcp.message.TCP_PACKET;

public class TaskPacketsRetrans extends TimerTask{
	private Client senderClient;
	private TCP_PACKET[] packets;
	
	public TaskPacketsRetrans(Client client, TCP_PACKET[] packetsRetrans) {
		super();
		senderClient = client;
		packets = packetsRetrans;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < packets.length; i++) {
			senderClient.send(packets[i]);
		}
	}
}
