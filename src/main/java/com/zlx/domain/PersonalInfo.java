package com.zlx.domain;

import java.io.Serializable;

import io.netty.channel.Channel;

public class PersonalInfo implements Serializable{
	private String Account;
	private String password;
	private Channel channel;
	private String login;
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	@Override
	public String toString() {
		return "PersonalInfo [Account=" + Account + ", password=" + password + ", channel=" + channel + "]";
	}
	
}
