package com.zlx.domain;

public class QQMessage{
	public String type = MessageType.MSG_TYPE_CHAT_P2P;
	public long from = 0;
	public String fromNick = "";
	public int fromAvatar = 1;
	public long to = 0; 
	public String content = ""; 
	public String sendTime = MyTime.geTime();
}
