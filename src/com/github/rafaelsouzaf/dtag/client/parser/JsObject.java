package com.github.rafaelsouzaf.dtag.client.parser;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author rafael.souza
 */
public class JsObject extends JavaScriptObject {

	protected JsObject() {
	}
	
	public final native String getObject(String name) /*-{
		return this.name;
	}-*/;
	
	public final native JsArray<JsObject> convertToArray(JsObject object) /*-{
		return new Array(object);
	}-*/;
		
}