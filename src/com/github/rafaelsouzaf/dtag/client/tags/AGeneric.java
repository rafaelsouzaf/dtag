package com.github.rafaelsouzaf.dtag.client.tags;

import com.github.rafaelsouzaf.dtag.client.DTagException;
import com.github.rafaelsouzaf.dtag.client.parser.ElementBean;
import com.github.rafaelsouzaf.dtag.client.parser.JsObject;
import com.github.rafaelsouzaf.dtag.client.parser.JsonResultObject;
import com.github.rafaelsouzaf.dtag.client.utility.ReplaceMarks;
import com.github.rafaelsouzaf.dtag.client.utility.SentJSONP;
import com.github.rafaelsouzaf.dtag.client.utility.URLConstructor;
import com.github.rafaelsouzaf.dtag.client.utility.Utility;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 	Clase abstracta utilizada por todos los elementos.
 *  Las clases que tienen comportamento distinto implementan su propia forma
 *  de mostrar contenido.
 * @author rafael.souza
 */
public abstract class AGeneric {

	/**
	 * Retorna la accion de busqueda del JSON. La accion será utilizada para
	 * generar la URL que irá buscar el JSON el en server.
	 * 
	 * @return Nombre de la accion
	 */
	protected abstract String getAction();

	/**
	 * Metodo que será llamado por la clase principal en el momento de
	 * parsear los elementos <DTAG:XX>. Implementación opcional.
	 * 
	 * @param e
	 * @throws DTagException
	 */
	public void execute(ElementBean e) throws DTagException {

		/**
		 * Validación
		 */
		if (Utility.isBlankOrNull(e.getSite())) {
			throw new DTagException("Atributo 'site' invalido o inexistente.");
		}
		if (Utility.isBlankOrNull(e.getId())) {
			throw new DTagException("Atributo 'Id' invalido o inexistente.");
		}

		/**
		 * Define URL
		 */
		URLConstructor url = new URLConstructor();
		url.addParameter(URLConstructor.ACTION, getAction());
		url.addParameter(URLConstructor.SITECODE, e.getSite());
		url.addParameter(URLConstructor.ID, e.getId());
		
		/**
		 * JSON
		 */
		sentJSON(e, "");

	}

	/**
	 * Envia el request JSON y implementa AsyncCallback para tratar en
	 * JavaScriptObject retornado. Implementación opcional.
	 * 
	 * @param e
	 * @param url
	 */
	public void sentJSON(final ElementBean e, String url) {

		/**
		 * JSON
		 */
		Window.alert("url: " + url);
		SentJSONP.sentJSON(url, new AsyncCallback<JsonResultObject>() {

			public void onFailure(Throwable caught) {
				Window.alert("onFailure: " + caught.getMessage());
			}

			public void onSuccess(JsonResultObject beanArray) {
				
				Window.alert("REspuesta: " + beanArray.toString());

				try {

					String finalHtml = "";

					JsArray<JsObject> beans = beanArray.getJsArray();
					GWT.log("length: " + beans.length());
					for (int i = 0; i < beans.length(); i++) {

						/**
						 * Verifica el valor del atributo "start" definido por
						 * el usuario y ignora la posicion si corresponde.
						 */
						if ((i + 1) < Integer.parseInt(e.getStart())) {
							continue;
						}

						JsObject bean = beans.get(i);
	

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
						//Element cloneElement = (Element) e.getElement().cloneNode(true);

						/**
						 * Procesa elemento clonado
						 */
						//Element newElement = process(cloneElement, bean);

						/**
						 * Concatena html actual con el elemento clonado y
						 * procesado
						 */
						//finalHtml += newElement.getInnerHTML();

					}

					/**
					 * Imprime en pantalla
					 */
					Utility.printAndDisplay(e.getElement(), finalHtml);

				} catch (Exception e) {
					Window.alert("Error in class Generic.onSucess(): " + e.getMessage());
				}

			}

		});

	}

	/**
	 * Procesa la instancia de JavaScriptObject. Implementación opcional.
	 * 
	 * @param element
	 * @param bean
	 * @return Element
	 * @throws DTagException
	 */
	public Element process(Element element, JsObject bean) throws DTagException {

		/**
		 * Reemplaza elemento principal Si contenido del bean es null, entonces
		 * ignora
		 */
		ReplaceMarks replaceTags = new ReplaceMarks();
		String html = replaceTags.replateDTAGTags(element.getInnerHTML(), bean);

		/**
		 * Imprime en la pagina
		 */
		Utility.printAndDisplay(element, html);

		return element;

	}

}
