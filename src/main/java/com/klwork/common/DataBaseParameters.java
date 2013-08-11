package com.klwork.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.util.ReflectionUtils;

import com.klwork.business.domain.model.DictDef;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;


public class DataBaseParameters {
	public static final int SINA = DictDef.dictInt("sina");
	
	public static final int TENCENT = DictDef.dictInt("tencent");
}
