package com.github.rafaelsouzaf.dtag.client.utility;

import com.github.rafaelsouzaf.dtag.client.parser.JsonResultObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SentJSONP {
	
	/**
	 * Envia una consulta por un JSON a un servidor externo.
	 * @param url
	 * @param callback
	 */
	public static void sentJSON(String url, AsyncCallback<JsonResultObject> callback) {
		sentJSON(url, callback, true);
	}
	
	/**
	 * Envia una consulta por un JSON a un servidor externo.
	 * @param url
	 * @param callback
	 * @param requestIsTAG
	 */
	public static void sentJSON(final String url, final AsyncCallback<JsonResultObject> callback, final boolean requestIsTAG) {
		
		/**
		 * Verifica si callback viene con error
		 */
		AsyncCallback<JsonResultObject> newCallback = new AsyncCallback<JsonResultObject>() {
			
			public void onFailure(Throwable caught) {

				
			}
			
			public void onSuccess(JsonResultObject result) {

			}
			
		};
		
		/**
		 * Envia request JSON
		 */
		try {
			JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
			jsonp.setTimeout(25000);
			jsonp.requestObject(url, newCallback);
		} catch (Exception e) {
			Window.alert(e.getMessage());;
		}
		
		/**
		 * Incrementa contador de n° de ejecuciones de la url
		 */
//		url.incrementExecuteCount();
		
	}

}
