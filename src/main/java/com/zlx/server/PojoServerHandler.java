package com.zlx.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlx.domain.QQMessage;
import com.zlx.domain.MessageType;
import com.zlx.domain.QQBuddy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class PojoServerHandler extends ChannelInboundHandlerAdapter {
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static List<QQBuddy> buddyList =new ArrayList();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
    	Channel incoming = ctx.channel();
    	QQMessage message = JSON.parseObject((String)msg, QQMessage.class);
    	JSONObject jsonObject = JSON.parseObject((String) msg);
    	if("login".equals(jsonObject.get("type"))){
    		message.type = MessageType.MSG_TYPE_LOGIN_SUCCESS;
    		QQBuddy buddy = new QQBuddy();
    		String[] a = message.content.split("#");
    		buddy.account= Integer.parseInt(a[0]);
    		buddy.avatar =0;
    		buddy.channel = incoming;
    		buddyList.add(buddy);
    		String strJson = JSON.toJSONString(message);
    		for(Channel channel : channels){
    			if(channel==incoming){
    				channel.writeAndFlush(strJson);
    			}
    		}
    	}else if("buddylist".equals(jsonObject.get("type"))){
    		String strBuddyList = JSON.toJSONString(buddyList);
    		message.content = strBuddyList;
    		message.type = MessageType.MSG_TYPE_BUDDY_LIST;
    		String strJson = JSON.toJSONString(message);
    		for(Channel channel : channels){
    			if(channel==incoming){
    				channel.writeAndFlush(strJson);
    			}
    		}
    	}else if("chatp2p".equals(jsonObject.get("type"))){
    		for(Channel channel : channels){
    			if(channel==incoming){
    				message.type = MessageType.MSG_TYPE_SEND;
    				String strJson = JSON.toJSONString(message);
    				channel.writeAndFlush(strJson);
    			}else{
    				message.type = MessageType.MSG_TYPE_RECEIVE;
    				String strJson = JSON.toJSONString(message);
    				for(QQBuddy buddy:buddyList){
    					if(buddy.account ==message.to){
    						channel.writeAndFlush(strJson);
    					}
    				}		
    			}			
    		}
    	}
    	
       
    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println(incoming.remoteAddress()+"在线");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.add(incoming);
		System.out.println(incoming.remoteAddress()+"加入");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.remove(incoming);
		removeBuddy(incoming);
		System.out.println(incoming.remoteAddress()+"离开");
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		removeBuddy(incoming);
		 System.out.println(incoming.remoteAddress()+"掉线");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel incoming = ctx.channel();
		removeBuddy(incoming);
		System.out.println(incoming.remoteAddress()+"异常");
		cause.printStackTrace();
		ctx.close();
	}

	private void removeBuddy(Channel incoming) {
		for(QQBuddy buddy:buddyList){
			if(buddy.channel==incoming){
				buddyList.remove(buddy);
			}
		}
	}
	
}