package com.github.rafaelsouzaf.dtag.client.tags;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.rafaelsouzaf.dtag.client.DTagException;
import com.github.rafaelsouzaf.dtag.client.parser.ElementBean;
import com.github.rafaelsouzaf.dtag.client.parser.JsonResultObject;
import com.github.rafaelsouzaf.dtag.client.utility.ReplaceMarks;
import com.github.rafaelsouzaf.dtag.client.utility.Utility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Get extends AGeneric {
	
	private int counter = 0;
	private ElementBean element;
	private Map<String, Object> map = new HashMap<String, Object>();
	
	
	
	public Get() {
		
	}

	@Override
	protected String getAction() {
		return null;
	}
	
	@Override
	public void execute(ElementBean e) throws DTagException {
		
		this.element = e;
				
		/**
		 * Validacion
		 */
		if (Utility.isBlankOrNull(e.getSrc())) {
			throw new DTagException("Atributo 'src' invalido o inexistente.");
		}

		/**
		 * JSON
		 */
		getJsonP();

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
			
			GWT.log(counter + " - Es Boolean: " + object.isBoolean().booleanValue() + ", Key: " + key);
			map.put(key, object.toString());
			
		} else if (object.isNull() != null) {
			
			GWT.log(counter + " - Es Null: " + object.toString() + ", Key: " + key);
			map.put(key, object.toString());
			
		} else if (object.isNumber() != null) {
			
			GWT.log(counter + " - Es Number: " + object.isNumber().toString() + ", Key: " + key);
			map.put(key, object.isNumber().toString());
			
		} else if (object.isString() != null) {
			
			GWT.log(counter + " - Es StringValue: " + object.isString().stringValue() + ", Key: " + key);
			map.put(key, object.isString().stringValue());
			
		} else {
			
			GWT.log(counter + " - Es Otra coisa. Key: " + key);
			
		}
		
	}
	
	private void getJsonP() {
		
		final String html = this.element.getElement().getInnerHTML();
		final Element element = this.element.getElement();
		
		AsyncCallback<JsonResultObject> newCallback = new AsyncCallback<JsonResultObject>() {
			
			/**
			 * Verifica si callback viene con error
			 */
			public void onFailure(Throwable caught) {
				String msg = caught.getMessage();
				Window.alert("interno: " + msg);
			}
			
			public void onSuccess(JsonResultObject result) {
				
				JsonResultObject.log(result.getObject());
				
				
				JavaScriptObject value = result.getJSONValuet();
				JSONValue parse = JSONParser.parseStrict(value.toString());
				processJson2(parse, null);
				
				for (Map.Entry<String, Object> entry : map.entrySet()) {
			        GWT.log("Key : " + entry.getKey() + " Value : " + entry.getValue());
			    }
				
				String finalHtml = new ReplaceMarks().replace(html, map);
				GWT.log(finalHtml);
				
				/**
				 * Imprime en pantalla
				 */
				Utility.printAndDisplay(element, finalHtml);
				
			}
			
		};
		
		/**
		 * Envia request JSON
		 */
		try {
			JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
			jsonp.setTimeout(25000);
			jsonp.requestObject(this.element.getSrc(), newCallback);
		} catch (Exception e) {
			Window.alert(e.getMessage());;
		}
		
	}

}
