package com.github.rafaelsouzaf.dtag.client.parser;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;

/**
 * @author rafael.souza
 *
 */
public class JsonResultObject extends JavaScriptObject {

	protected JsonResultObject() {
	}

	public final native JsArray<JsObject> getJsArray() /*-{
		console.log(this);
		if(Object.prototype.toString.call(this) === '[object Array]') {
		    alert('Array!');
		    return this;
		} else {
			alert('Object!');
			return new Array(this);
		}
	}-*/;
	
	public final native JsObject getObject() /*-{
		console.log(this);
		return this;
	}-*/;
	
	public final native JavaScriptObject getJSONValuet() /*-{
		return JSON.stringify(this);;
	}-*/;
	
	public final static native boolean isArray(Object object) /*-{
		if(Object.prototype.toString.call(object) === '[object Array]') {
		    alert('Yes!');
		    return true;
		} else {
			alert('No!');
			return false;
		}
	}-*/;
	
	public final static native void log(Object object) /*-{
		console.log(object);
	}-*/;
	
	/*
	 * Takes in a JSON String and evals it.
	 * @param JSON String that you trust
	 * @return JavaScriptObject that you can cast to an Overlay Type
	 */
	public static <T extends JavaScriptObject> T parseJson(String jsonStr)
	{
	  return JsonUtils.safeEval(jsonStr);
	}

}