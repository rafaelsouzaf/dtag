package com.github.rafaelsouzaf.dtag.client.parser;

import com.github.rafaelsouzaf.dtag.client.utility.Utility;
import com.google.gwt.dom.client.Element;

/**
 * Clase Bean utilizado para mantener todos los datos que llegan
 * con el request. Convierte los parametros del request a
 * objectos del bean.
 * @author rafael.souza
 */
public class ElementBean {

	private Element element;
	private String src;
	
	private String id;
	private String site;
	private String start;
	private String limit;
	private String time;
	private String category;
	private String taxonomy;
	private String type;
	private String edition;
	private String page;
	private String position;
	private String beginDate;
	private String endDate;

	/**
	 * @param element
	 */
	public ElementBean(Element element) {
		this.element = element;
		id = Utility.getTagAttribute(element, "id");
		src = Utility.getTagAttribute(element, "src");
		
		site = Utility.getTagAttribute(element, "site");
		start = Utility.getTagAttribute(element, "start", "1");
		limit = Utility.getTagAttribute(element, "limit", "10");
		time = Utility.getTagAttribute(element, "time", "168");
		category = Utility.getTagAttribute(element, "category");
		taxonomy = Utility.getTagAttribute(element, "taxonomy");
		type = Utility.getTagAttribute(element, "type");
		edition = Utility.getTagAttribute(element, "edition");
		page = Utility.getTagAttribute(element, "page");
		position = Utility.getTagAttribute(element, "position");
		beginDate = Utility.getTagAttribute(element, "begin_date");
		endDate = Utility.getTagAttribute(element, "end_date");
	}

	public Element getElement() {
		return element;
	}

	public String getId() {
		return id;
	}

	public String getSite() {
		return site;
	}

	public String getLimit() {
		return limit;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTime() {
		return time;
	}

	public String getCategory() {
		return category;
	}

	public String getTaxonomy() {
		return taxonomy;
	}

	public String getType() {
		return type;
	}

	public String getEdition() {
		return edition;
	}

	public String getPage() {
		return page;
	}

	public String getPosition() {
		return position;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

}
