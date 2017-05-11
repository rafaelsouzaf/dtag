package com.github.rafaelsouzaf.dtag.client.utility;

import java.util.HashMap;
import java.util.Map;

import com.github.rafaelsouzaf.dtag.client.parser.JsObject;
import com.google.gwt.core.client.GWT;

public class ReplaceMarks {

	/**
	 * Reemplaza todas las marcas [] soportadas por su respectivo valor.
	 * @param html
	 * @param bean
	 * @return Retorna el HTML con las marcas[] reemplazadas por su respectivo valor.
	 */
	public String replace(String html, Map<String, Object> map) {
		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
	        GWT.log("Key : " + entry.getKey() + " Value : " + entry.getValue());
	    }
		
		return replaceTags(map, html);
		
	}
	
	public String replateDTAGTags(String html, JsObject bean) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		/*
		map.put("type", bean.getType());
		map.put("siteCode", bean.getSitecode());
		map.put("site", bean.getSitecode());
		map.put("id", bean.getId());
		map.put("title", bean.getTitle());
		map.put("path", bean.getPath());
		map.put("taxonomy", bean.getTaxonomy());
		map.put("limit", bean.getLimit());
		map.put("edition", bean.getEdition());
		map.put("page", bean.getPage());
		map.put("description", bean.getDescription());
		map.put("url", bean.getUrl());
		map.put("note", bean.getNote());
		map.put("abstracttext", bean.getAbstracttext());
		map.put("shortdescription", bean.getShortdescription());
		map.put("file", bean.getFile());
		map.put("width", bean.getWidth());
		map.put("height", bean.getHeight());
		map.put("author", bean.getAuthor());
		map.put("htmlbox", bean.getHtmlbox());
		map.put("mail", bean.getMail());
		map.put("firstname", bean.getFirstname());
		map.put("lastname", bean.getLastname());
		map.put("fullname", bean.getFullname());
		map.put("image", bean.getImage());
		map.put("image_author", bean.getImageAuthor());
		map.put("image_description", bean.getImageDescription());
		map.put("biography", bean.getBiography());
		map.put("scroll", bean.getScroll());
		map.put("name", bean.getName());
		map.put("hits", bean.getHits());
		map.put("edition_id", bean.getEditionId());
		map.put("edition_date", bean.getEditionDate());
		map.put("edition_version", bean.getEditionVersion());
		map.put("begin_date", bean.getBeginDate());
		map.put("end_date", bean.getEndDate());
		*/

		/**
		 * Reemplaza Date
		 */
		//replaceDate(map, bean.getDate());

		/**
		 * Reemplaza otros
		 */
		html = replaceTags(map, html);

		/**
		 * reemplaza [textos] que no poseen correspondente en json y retorna
		 */
		return removeUnusedTags(html);

	}

	/**
	 * Reemplaza [marca] por valor
	 * 
	 * @param map
	 * @param html
	 * @return Retorna el HTML con las [marcas] reemplazadas por su respectivo valor.
	 */
	private String replaceTags(Map<String, Object> map, String html) {
		for (Map.Entry<String, Object> separa : map.entrySet()) {
			if (separa.getValue() == null) {
				continue;
			}
			if (html.contains("[" + separa.getKey() + "]")) {
				html = html.replace("[" + separa.getKey() + "]", separa.getValue() + "");
			}
			if (html.contains("%5B" + separa.getKey() + "%5D")) {
				html = html.replace("%5B" + separa.getKey() + "%5D", separa.getValue() + "");
			}
		}
		return html;
	}


	/**
	 * Metodo javascript nativo que remove todas las marcas [] que no seran
	 * utilizadas. Util para mantener el diseño limpio de tags que no existen
	 * o que no tiene contenido para asociar.
	 * @param tmpHtml
	 * @return Retorna codigo HTML sin mas marcas []
	 */
	private final static native String removeUnusedTags(String tmpHtml) /*-{
		return tmpHtml.replace(/\[.*?\]/g, "");
//		return tmpHtml.replace(/\[\D*\]/, "");
	}-*/;

}
