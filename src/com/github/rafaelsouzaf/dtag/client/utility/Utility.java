package com.github.rafaelsouzaf.dtag.client.utility;

import java.util.LinkedList;
import java.util.List;

import com.github.rafaelsouzaf.dtag.client.DTag;
import com.github.rafaelsouzaf.dtag.client.parser.JsObject;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.Window.Navigator;

/**
 * @author rafael.souza
 */
public class Utility {
	
	/**
	 * Realiza una busqueda por una TAG HTML dentro de un elemento HTML. Ambos pasados por parametro.
	 * @param element Elemento donde se desea hacer la busqueda.
	 * @param tag Tag HTML que se desea encontrar.
	 * @return Retorna una lista de elementos HTML
	 */
	public static List<Element> getElementsByTagName(Element element, String tag) {
		
		List<Element> list = new LinkedList<Element>();
		NodeList<Element> childNodes = null;
		
		/**
		 * Si es Internet Explorer, entonces tiene que buscar sin el prefijo DTAG:
		 */
		if (Navigator.getUserAgent().toUpperCase().contains("MSIE")) {
			childNodes = element.getElementsByTagName(tag.toUpperCase());
		} else {
			childNodes = element.getElementsByTagName("DTAG:" + tag.toUpperCase());
		}
		
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				/**
				 * Si tag es IMG, ignora. Por algun motivo el maldito IE cuando hace busqueda
				 * por tag IMAGE, tambien retorna las tags IMG que existe en el documento
				 */
				if (childNodes.getItem(i).getTagName().equalsIgnoreCase("IMG")) {
					continue;
				}
				
				/**
				 * Si ya fue procesado, ignora
				 */
				String className = childNodes.getItem(i).getClassName();
				if (className.contains(DTag.PROCESSED)) {
					continue;
				}
				
				list.add(childNodes.getItem(i));
			}
		}
		
		return list;
		
	}
	
	/**
	 * Convierte un objecto JsArray en una lista de objectos JavaScriptObject
	 * @param jsArray
	 * @return Retorna una lista de List<JavaScriptObject> 
	 */
	public static List<JavaScriptObject> convertJsArrayInList(JsArray jsArray) {
		List<JavaScriptObject> list = new LinkedList<JavaScriptObject>();
		if (jsArray != null) {
			for(int i=0;i<jsArray.length();i++) {
				list.add(jsArray.get(i));
			}
		}
		return list;
	}
	
	/**
	 * @param elements
	 * @param json
	 */
	public static void processOther(List<Element> elements, JsArray<JsObject> json) {
		
		for (Element element: elements) {

			/**
			 * IE genera confusion con algunas tags como por ejemplo DTAG:IMG (nuestra) y IMG (HTML normal)
			 * para evitar problemas se debe proseguir solamente si la tag contiene
			 * alguno contenido, sino es probable que sea IMG (HTML).
			 * También para ignorar contenidos que no tengan nada que procesar
			 */
			if (Utility.isBlankOrNull(element.getInnerHTML())) {
				continue;
			}
			if (!Utility.hasDTagMark(element.getInnerHTML())) {
				continue;
			}
			
			/**
			 * La tag puede ser repetible, en los casos de listar imagenes
			 * de las galerias por ejemplo, en ese caso se debe copiar
			 * el contenido inicial y repetir ese contenido reemplazando
			 * los datos por los que corresponde.
			 */
			String contenidoFinal = "";
			
			if (json != null) {
				ReplaceMarks replaceTags = new ReplaceMarks();
				for(int i=0; i<json.length(); i++) {
					
					Element cloneElement = (Element) element.cloneNode(true);
					JsObject beanInternal = json.get(i);
					
					contenidoFinal += replaceTags.replateDTAGTags(cloneElement.getInnerHTML(), beanInternal);
					
				}
			}
			
			Utility.printAndDisplay(element, contenidoFinal);
			
		}
		
	}

	/**
	 * Carga elemento con nuevo código HTML (procesado)
	 * y cambia el css style para 'display: inline'
	 * @param element
	 * @param html
	 */
	public static void printAndDisplay(Element element, String html) {
		print(element, html);
		display(element, Display.INLINE);
	}
	
	/**
	 * Carga elemento con nuevo código HTML (procesado)
	 * @param element
	 * @param html
	 */
	public static void print(Element element, String html) {
		element.setInnerHTML(html);
	}
	
	/**
	 * Cambia el css style para el tipo seleccionado, ej: 'display: inline;'
	 * Y marca elemento como procesado 'class="processed"'
	 * @param element
	 * @param type
	 */
	public static void display(Element element, Display type) {
		element.addClassName(DTag.PROCESSED);
		element.getStyle().setDisplay(type);
	}
	
	/**
	 * Verifica si string tiene contenido nulo o en blanco.
	 * @param string
	 * @return Retorna true si el string es nulo o vacio
	 */
	public static boolean isBlankOrNull(String string) {
		if (string == null || string.trim().equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Busca por un atributo dentro de la URL de la página. Es utilizado para
	 * realizar despliegues de contenido cuando los datos (ID, SITE, etc) son
	 * pasados por parametro de URL
	 * @param element
	 * @param attribute
	 * @return Retorna el valor del atributo
	 */
	public static String getTagAttribute(Element element, String attribute) {
		return getTagAttribute(element, attribute, "");
	}
	
	/**
	 * Busca por un atributo dentro de la URL de la página. Es utilizado para
	 * realizar despliegues de contenido cuando los datos (ID, SITE, etc) son
	 * pasados por parametro de URL
	 * @param element
	 * @param attribute
	 * @param defaultValue
	 * @return Retorna el valor del atributo
	 */
	public static String getTagAttribute(Element element, String attribute, String defaultValue) {
		String retorno = "";
		try {
			retorno = element.getAttribute(attribute);
			if (retorno.equals("?")) {
				retorno = Location.getParameter(attribute);
			}
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		if (Utility.isBlankOrNull(retorno)) {
			return defaultValue;
		}
		return retorno;
	}
	
	/**
	 * Metodo javascript nativo que averigua si dentro de un determinado
	 * contenido HTML hay marcas []
	 * @param tmpHtml
	 * @return Retorna true si hay [marcas] o false si no hay.
	 */
	public final static native boolean hasDTagMark(String tmpHtml) /*-{
		if (tmpHtml.match(/\[\D*\]/)) {
			return true;
		}
		return false;
	}-*/;
	
	/**
	 * Metodo javascript nativo que retorna todos los atributos de un
	 * elemento HTML. Cuidado con el uso porque puede haber incompatibilidad
	 * con el maldito Internet Explorer.
	 * @param elem
	 * @return Retorna objecto JsArray<Node>
	 */
 	public static native JsArray<Node> getAttributes(Element elem) /*-{
		return elem.attributes;
	}-*/;
	
}
