package com.github.rafaelsouzaf.dtag.client;

import com.github.rafaelsouzaf.dtag.client.tags.AGeneric;
import com.github.rafaelsouzaf.dtag.client.tags.Get;

/**
 * Lista de elementos TAGS soportadas.
 */
public enum DTagSoported {
	
	GET(new Get(), null);

	private AGeneric instance;
	private String alternativeName;

	/**
	 * @param instance
	 */
	private DTagSoported(AGeneric instance, String alternativeName) {
		this.instance = instance;
		this.alternativeName = alternativeName;
	}

	/**
	 * @return Retorna una instancia del elemento
	 */
	public AGeneric getInstance() {
		return instance;
	}
	
	public String getAlternativeName() {
		if (this.alternativeName != null) {
			return this.alternativeName;
		}
		return this.name();
		
	}

}
