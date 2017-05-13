package com.github.rafaelsouzaf.dtag.client.tags;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.rafaelsouzaf.dtag.client.DTagException;
import com.github.rafaelsouzaf.dtag.client.bean.JsonLogic;
import com.github.rafaelsouzaf.dtag.client.parser.ElementBean;
import com.github.rafaelsouzaf.dtag.client.parser.JsonResultObject;
import com.github.rafaelsouzaf.dtag.client.utility.Notification;
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
	private JsonLogic tree = new JsonLogic();
	
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
	
	private void processJson2(JSONValue node, String key) {
		
		counter++;
		
		//GWT.log(object.isObject() + "");
		// if objecto es array, loop y vuelve a procesar
		// if objecto es objecto, imprime
		if (node.isArray() != null) {
			GWT.log(counter + " - Array. Key: " + key);
			JSONArray tempArray = node.isArray();
			for (int i=0; i<tempArray.size(); i++) {		
				processJson2(tempArray.get(i), null);
			}
		} else if (node.isObject() != null) {
			GWT.log(counter + " - Object. Key: " + key);
			tree.addNewArray("asdasdasdasdasd");
			Set<String> keySet = node.isObject().keySet();
			for (String string : keySet) {
				JSONValue tempV = node.isObject().get(string);
				processJson2(tempV, string);
			}
		} else if (node.isBoolean() != null) {
			
			GWT.log(counter + "		### Boolean. " + key + "	->		" + node.isBoolean().booleanValue());
			tree.addKeyValueItemToActualArray(key, node.isBoolean().booleanValue());
			
		} else if (node.isNull() != null) {
			
			GWT.log(counter + "		### Null. " + key + "->" + node.toString());
			
		} else if (node.isNumber() != null) {
			
			GWT.log(counter + "		### Number. " + key + "->" + node.isNumber().toString());
			tree.addKeyValueItemToActualArray(key, node.isNumber().toString());
			
		} else if (node.isString() != null) {
			
			GWT.log(counter + "		### String. " + key + "->" + node.isString().stringValue());
			tree.addKeyValueItemToActualArray(key, node.isString().stringValue());
			
		} else {
			
			GWT.log(counter + " - Es Otra coisa. Key: " + key);
			
		}
		
	}
	
	private void getJsonP() {
		
		final Element newE = this.element.getElement();
		
		AsyncCallback<JsonResultObject> newCallback = new AsyncCallback<JsonResultObject>() {
			
			/**
			 * Verifica si callback viene con error
			 */
			public void onFailure(Throwable caught) {
				String msg = caught.getMessage();
				Window.alert("interno: " + msg);
			}
			
			public void onSuccess(JsonResultObject result) {
				
				try {
					
					String finalHtml = "";
					
					/**
					 * Convert Json to Object
					 */
					JsonResultObject.log(result.getObject());
					JavaScriptObject value = result.getJSONValuet();
					JSONValue parse = JSONParser.parseStrict(value.toString());
					processJson2(parse, null);
					
					List<Object> list = tree.getList();
					GWT.log("LISTA: " + list.size());
					for (Object object : list) {
						
						/**
						 * Clona elemento original La clonación es fundamental
						 * porque puede que el JSON retorne un arreglo de
						 * elementos, y es necesario procesar todos los
						 * elementos del arreglo con el HTML sin modificación.
						 * Si no clonamos, el HTML procesado por el primer
						 * elemento no seria util para los siguientes. Con la
						 * clonación dejamos el HTML original impecable para ser
						 * procesador por todos los elementos que retorna el
						 * JSON.
						 */
						Element cloneElement = (Element) newE.cloneNode(true);
						
						/**
						 * Reemplaza data 
						 */
						ReplaceMarks replaceTags = new ReplaceMarks();
						String html = replaceTags.replaceTags((Map<String, Object>) object, newE.getInnerHTML());
						
						GWT.log(html);
						
						/**
						 * Concatena html actual con el elemento clonado y
						 * procesado
						 */
						finalHtml += html;
						
						
						
						
						
					}
					
					/**
					 * Imprime en pantalla
					 */
					Utility.printAndDisplay(newE, finalHtml);
										
				} catch (Exception e) {
					Notification.error("Error in class AbstractGenericTag.onSucess(): " + e.getMessage(), e);
				}
				
				//GWT.log("-------------- Tree ------------------");
				//tree.print();
				
				//GWT.log("-------------- finalHtml ------------------");
				//String finalHtml = new ReplaceMarks().replaceTags(map, html);
				//List<Object> list = tree.getList();
				//String finalHtml = new ReplaceMarks().replaceTags(list, html);
				//GWT.log(finalHtml);
				
				/**
				 * Imprime en pantalla
				 */
				//Utility.printAndDisplay(element, finalHtml);
				
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
