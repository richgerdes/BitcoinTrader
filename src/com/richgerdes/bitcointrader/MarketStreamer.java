package com.richgerdes.bitcointrader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.drafts.Draft_17;
import org.json.simple.JSONObject;

public class MarketStreamer extends Market{
	
	private MtGoxWebSocketHandle mtGoxHandle;

	public MarketStreamer(){
		Map<String, String> headers = new HashMap<String, String>();
		
		/*
		 * header requirements
		 * 
		 * GET /mtgox HTTP/1.1		
		 * Upgrade: websocket		
		 * Connection: Upgrade
		 * Host: websocket.mtgox.com
		 * Origin: ws://websocket.mtgox.com
		 * Sec-WebSocket-Key: 6omOcZl7/BelBTW0PIM8xQ==  //TODO Unknown key needs to be replaced
		 * Sec-WebSocket-Protocol: mtgox
		 * Sec-WebSocket-Version: 13
		 * 
		 */
		
		headers.put("Upgrade", "WebSocket");
		headers.put("Connection", "Upgrade");
		headers.put("Host", "websocket.mtgox.com");
		headers.put("Origin", "ws://websocket.mtgox.com");
		headers.put("Sec-WebSocket-Key", "6omOcZl7/BelBTW0PIM8xQ==");
		headers.put("Sec-WebSocket-Protocol", "mtgox");
		headers.put("Sec-WebSocket-Version", "13");
		
		try {
			mtGoxHandle = new MtGoxWebSocketHandle(new URI("ws://websocket.mtgox.com/mtgox"), new Draft_17(), headers);
			mtGoxHandle.connectBlocking();
			
			//mtGoxHandle.unsubscribe("24e67e0d-1cad-4cc0-9e7a-f8523ef460fe");
			//mtGoxHandle.unsubscribe("dbf1dee9-4f2e-4a08-8cb7-748919a71b21");
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		if(mtGoxHandle == null)
			return;
		
		System.out.println("waiting");
		JSONObject read = null;
		while(true){
			read = mtGoxHandle.next();
			if(read == null){
				//System.out.println("reading null...");
				continue;
			}
			//System.out.println(read.get("private").toString());
			String type = read.get("private").toString();
			if(type.equals("ticker")){
				JSONObject ticker = (JSONObject) read.get("ticker");
				JSONObject last = (JSONObject) ticker.get("last");
				//System.out.println(last.toJSONString());
				float value = Float.parseFloat(last.get("value").toString());
				System.out.println("ticker " + value);
				tradeAt(value);
			}else if(type.equals("depth")){
				continue; //Analyse Depth?
			}else if(type.equals("trade")){
				JSONObject trade = (JSONObject) read.get("trade");
				//System.out.println(trade.toJSONString());
				float value = Float.parseFloat(trade.get("price").toString());
				System.out.println("trade " +  value);
				//tradeAt(value);
			}else{
				continue;
			}
		}
	}
	
}
