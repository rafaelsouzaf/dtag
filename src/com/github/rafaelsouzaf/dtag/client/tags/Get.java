package com.github.rafaelsouzaf.dtag.client.tags;

import java.util.Set;

import com.github.rafaelsouzaf.dtag.client.DTagException;
import com.github.rafaelsouzaf.dtag.client.parser.ElementBean;
import com.github.rafaelsouzaf.dtag.client.parser.JsonResultObject;
import com.github.rafaelsouzaf.dtag.client.utility.Utility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Get extends AGeneric {
	
	private int counter = 0;

	@Override
	protected String getAction() {
		return null;
	}
	
	@Override
	public void execute(ElementBean e) throws DTagException {
				
		/**
		 * Validacion
		 */
		if (Utility.isBlankOrNull(e.getSrc())) {
			throw new DTagException("Atributo 'src' invalido o inexistente.");
		}

		/**
		 * JSON
		 */
		getJsonP(e.getSrc());

	}
	
	private void processJson2(JSONValue object, String key) {
		
		counter++;
		
		//GWT.log(object.isObject() + "");
		// if objecto es array, loop y vuelve a procesar
		// if objecto es objecto, imprime
		if (object.isArray() != null) {
			GWT.log(counter + " - Es Array. Key: " + key);
			JSONArray tempArray = object.isArray();
			for (int i=0; i<tempArray.size(); i++) {
				processJson2(tempArray.get(i), key);
			}
		} else if (object.isObject() != null) {
			GWT.log(counter + " - Es objecto. Key: " + key);
			// lista propiedades del objecto
			// si alguna es array, procesa nuevamente
			// sino, imprime
			Set<String> keySet = object.isObject().keySet();
			for (String string : keySet) {
				JSONValue tempV = object.isObject().get(string);
				processJson2(tempV, string);
			}
		} else if (object.isBoolean() != null) {
			
			GWT.log(counter + " - Es Boolean: " + object.toString() + ", Key: " + key);
			
		} else if (object.isNull() != null) {
			
			GWT.log(counter + " - Es Null: " + object.toString() + ", Key: " + key);
			
		} else if (object.isNumber() != null) {
			
			GWT.log(counter + " - Es Number: " + object.toString() + ", Key: " + key);
			
		} else if (object.isString() != null) {
			
			GWT.log(counter + " - Es String: " + object.toString() + ", Key: " + key);
			
		} else {
			
			GWT.log(counter + " - Es Otra coisa. Key: " + key);
			
		}
		
	}
	
	private void getJsonP(String url) {
		
		AsyncCallback<JsonResultObject> newCallback = new AsyncCallback<JsonResultObject>() {
			
			/**
			 * Verifica si callback viene con error
			 */
			public void onFailure(Throwable caught) {
				
				String msg = caught.getMessage();
				Window.alert("interno: " + msg);
				
			}
			
			public void onSuccess(JsonResultObject result) {
				
				JavaScriptObject value = result.getJSONValuet();
				JSONValue parse = JSONParser.parseStrict(value.toString());
				processJson2(parse, null);

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
	}

}
