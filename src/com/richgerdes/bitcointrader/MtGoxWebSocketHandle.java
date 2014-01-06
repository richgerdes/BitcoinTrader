package com.richgerdes.bitcointrader;

import java.net.URI;
import java.util.Map;
import java.util.Vector;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MtGoxWebSocketHandle extends WebSocketClient{
	
	private Vector<JSONObject> JSONList = new Vector<JSONObject>();
	
	public MtGoxWebSocketHandle(URI serverUri, Draft draft, Map<String, String> headers) {
		super(serverUri, draft, headers);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " by " + ((remote) ? "Server" : "Self") + ". additional info: " + reason);
	}

	@Override
	public void onError(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void onMessage(String msg) {
		//System.out.println(msg);
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(msg);
			JSONList.add(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onOpen(ServerHandshake h){
		System.out.println("Connection complete >> " + h.getHttpStatusMessage());

	}
	
	public void unsubscribe(String channel) {
		JSONObject obj=new JSONObject();
		obj.put("op","unsubscribe");
		obj.put("channel", channel);
		send(obj.toJSONString());
		System.out.println("sent");
	}
	
	public void subscribe(String channel) {
		JSONObject obj=new JSONObject();
		obj.put("op","subscribe");
		obj.put("channel", channel);
		send(obj.toJSONString());
		System.out.println("sent");
	}

	public JSONObject next(){
		if(JSONList.size() > 0){
			JSONObject j = JSONList.get(0);
			JSONList.remove(0);
			return j;
		}else
			return null;
	}

}
