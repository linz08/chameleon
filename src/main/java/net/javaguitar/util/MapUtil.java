package net.javaguitar.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);

	/**
	 * 맵을 VO로 변환
	 * 
	 * @param map
	 * @param obj
	 * @return
	 */
	public static Object convertMapToObject(Map<String, Object> map, Object obj) throws Exception {
		String keyAttribute = null;
		String setMethodString = "set";
		String methodString = null;
		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			keyAttribute = itr.next();
			methodString = setMethodString + keyAttribute.substring(0, 1).toUpperCase() + keyAttribute.substring(1);
			Method[] methods = obj.getClass().getMethods();

			for (int i = 0; i < methods.length; i++) {
				if (methodString.equals(methods[i].getName())) {
					try {
						methods[i].invoke(obj, map.get(keyAttribute));
					} catch (NullPointerException e) {
						LOGGER.error(e.getMessage());
					}
				}
			}

		}
		return obj;
	}

	/**
	 * 맵을 String VO로 변환
	 * 
	 * @param map
	 * @param obj
	 * @return
	 */
	public static Object convertMapToStringVO(Map<String, Object> map, Object obj) throws Exception {
		String keyAttribute = null;
		String setMethodString = "set";
		String methodString = null;
		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			keyAttribute = itr.next();
			methodString = setMethodString + keyAttribute.substring(0, 1).toUpperCase() + keyAttribute.substring(1);
			Method[] methods = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methodString.equals(methods[i].getName())) {
					try {
						methods[i].invoke(obj, map.get(keyAttribute).toString());
					} catch (NullPointerException e) {
						LOGGER.error(e.getMessage());
					}
				}
			}

		}

		return obj;
	}

	/**
	 * 맵을 xss 필터링 String VO로 변환
	 * 
	 * @param map
	 * @param obj
	 * @return
	 */
	public static Object convertMapToXssStringVO(Map<String, Object> map, Object obj) throws Exception {
		String keyAttribute = null;
		String setMethodString = "set";
		String methodString = null;
		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			keyAttribute = itr.next();
			methodString = setMethodString + keyAttribute.substring(0, 1).toUpperCase() + keyAttribute.substring(1);
			Method[] methods = obj.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methodString.equals(methods[i].getName())) {
					try {
						methods[i].invoke(obj, cleanXss(map.get(keyAttribute).toString()));
					} catch (NullPointerException e) {
						LOGGER.error(e.getMessage());
					}
				}
			}

		}

		return obj;
	}

	/**
	 * String xss 필터
	 * 
	 * @param value
	 * @return
	 */
	private static String cleanXss(String value) {
		value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
		value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
		value = value.replaceAll("'", "& #39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		return value;
	}
}
