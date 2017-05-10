package com.github.rafaelsouzaf.dtag.client;

import java.util.List;

import com.github.rafaelsouzaf.dtag.client.parser.ElementBean;
import com.github.rafaelsouzaf.dtag.client.tags.AGeneric;
import com.github.rafaelsouzaf.dtag.client.utility.Utility;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DTag implements EntryPoint {
	
	public static final String PROCESSED = "processed";

	public void onModuleLoad() {

		for (DTagSoported soportedTag : DTagSoported.values()) {

			List<Element> elements = Utility.getElementsByTagName(RootPanel.getBodyElement(), soportedTag.name());
			for (Element element : elements) {
				try {
					ElementBean e = new ElementBean(element);
					if (!e.getPosition().equals("")) {
						//continue;
					}
					AGeneric tag = soportedTag.getInstance();
					tag.execute(e);
				} catch (Exception e) {
					Window.alert(e.getMessage());
				}

			}

		}

	}
}
