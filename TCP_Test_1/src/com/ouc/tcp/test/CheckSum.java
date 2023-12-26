package com.ouc.tcp.test;

import java.util.zip.CRC32;

import com.ouc.tcp.message.TCP_HEADER;
import com.ouc.tcp.message.TCP_PACKET;
import com.ouc.tcp.message.TCP_SEGMENT;

public class CheckSum {
	
	/*计算TCP报文段校验和：只需校验TCP首部中的seq、ack和sum，以及TCP数据字段*/
	public static short computeChkSum(TCP_PACKET tcpPack) {
		CRC32 crc32 = new CRC32();
		TCP_HEADER tcpH = tcpPack.getTcpH();
		TCP_SEGMENT tcpS = tcpPack.getTcpS();
		crc32.update(tcpH.getTh_seq());
		crc32.update(tcpH.getTh_ack());
		for(int i = 0; i < tcpS.getData().length; i++) {
			 crc32.update(tcpS.getData()[i]);
		}
		return (short) crc32.getValue();
	}
	
}
