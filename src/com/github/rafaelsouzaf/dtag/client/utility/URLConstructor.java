package com.github.rafaelsouzaf.dtag.client.utility;

import java.util.HashMap;
import java.util.Map;

import com.github.rafaelsouzaf.dtag.client.DTag;

public class URLConstructor {
	
	/**
	 * Inicia contador
	 */
//	private static int MIRROR_COUNT = 1;
//	private static int MIRROR_COUNT_MAX = 5;
	
	/**
	 * Constantes utilizadas para formar la URL
	 */
	public static final String ACTION = "action";
	public static final String SITECODE = "siteCode";
	public static final String ID = "id";
	public static final String BEGIN_DATE = "begin_date";
	public static final String END_DATE = "end_date";
	public static final String CATEGORY = "category";
	public static final String TAXONOMY = "taxonomy";
	public static final String EDITION = "edition";
	public static final String PAGE = "page";
	public static final String LIMIT = "limit";
	public static final String TIME = "time";
	
	/**
	 * Crea mapa con llave/valor de los parametros de la ulr
	 */
	private Map<String, String> mapUrl = new HashMap<String, String>();
	
	/**
	 * Contador de ejecuciones por instancia
	 */
	private int executeCount = 0;
	
	/**
	 * Ultimo mirror ejecutado. Sirve para que al incrementar el
	 * mirror(variable estatica) de esta instancia, no repita el
	 * mismo valor.
	 */
	private int lastMirror = 0;
	
	/**
	 * Retorna el numero de ejecuciones realizadas por esta instancia
	 * @return
	 */
	public int getExecuteCount() {
		return this.executeCount;
	}
	
	public void incrementExecuteCount() {
		this.executeCount++;
	}
	
	/**
	 * Agrega parametro/valor a url
	 * @param parameter
	 * @param value
	 */
	public void addParameter(String parameter, String value) {
		mapUrl.put(parameter, value);
	}
	
	public void addParameterURL(String urlParameter) {
		String[] listParameters = urlParameter.split("&");
		if (listParameters == null || listParameters.length <= 0) {
			return;
		}
		for (String string: listParameters) {
			String[] param = string.split("=");
			if (param == null || param.length <= 0) {
				continue;
			}
			addParameter(param[0], param[1]);
		}
	}
	
	/**
	 * Incrementa numero del mirror
	 */
//	private void nextMirrorCount() {
//		MIRROR_COUNT++;
//		if (MIRROR_COUNT > MIRROR_COUNT_MAX) {
//			MIRROR_COUNT = 1;
//		}
//	}

}
