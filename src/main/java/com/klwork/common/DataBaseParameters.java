package com.klwork.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.util.ReflectionUtils;

import com.klwork.business.domain.model.DictDef;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;


public class DataBaseParameters {

	// 考核周期类别 每年
	public static String TEST_SIGN_VALUE;

	protected static final Logger logger = LoggerFactory
			.getLogger(DataBaseParameters.class);

	static {
		try {
			Field[] fields = DataBaseParameters.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field tfield = fields[i];
				if (isPublicStatic(tfield)) {
					String name = tfield.getName();
					logger.debug("参数初始化----" + name);
					DictDef def = DictDef.queryAllDefMaps().get(name);
					if (def != null) {
						ReflectionUtils.setField(tfield,
								DataBaseParameters.class, def.getValue());
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到一个类的静态属性
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isPublicStatic(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers));
	}
}
