package com.ouc.tcp.test;

import java.util.Timer;

import com.ouc.tcp.client.Client;
import com.ouc.tcp.message.TCP_PACKET;

public class SenderWindow {
	private Client client;
	private int wnd_size;
	private int nextIndex;
	
	private TCP_PACKET[] packets;
	
	private Timer timer;
	private TaskPacketsRetrans task;
	public SenderWindow(Client client, int size) {
		this.client = client;
		this.wnd_size = size;
		this.nextIndex = 0;
		this.packets = new TCP_PACKET[this.wnd_size];
	}
	
	public boolean isFull() {
		if (this.nextIndex == this.wnd_size)
			return true;
		else
			return false;
	}
	
	public boolean putIn(TCP_PACKET packet) {
		if (this.isFull())
			return false;
		//TODO: add a timer.
		if (this.nextIndex == 0) {
			this.timer = new Timer();
			this.task = new TaskPacketsRetrans(this.client, this.packets);
			this.timer.schedule(this.task, 3000, 3000);
		}
		this.packets[this.nextIndex] = packet;
		this.nextIndex ++;
		return true;
	}
	
	private void slideWindow(int len) {
		int i;
		for (i = 0; len + i < this.wnd_size; i ++) {
			this.packets[i] = this.packets[len + i];
		}
		for (; i < this.wnd_size; i ++) {
			this.packets[i] = null;
		}
		this.nextIndex -= len;
	}
	
	public void recvACK(int ackSeq) {
		int baseSeq = this.packets[0].getTcpH().getTh_seq();
		if (ackSeq >= baseSeq) {
			int len = (ackSeq - baseSeq) / 100 + 1;
			this.slideWindow(len);
			this.timer.cancel();
			if (this.nextIndex != 0) {
				this.timer = new Timer();
				this.task = new TaskPacketsRetrans(this.client, this.packets);
				this.timer.schedule(this.task, 3000, 3000);
			}
		}
	}
}
