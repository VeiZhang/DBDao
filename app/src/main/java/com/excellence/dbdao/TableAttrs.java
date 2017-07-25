package com.excellence.dbdao;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/7/25
 *     desc   :
 * </pre>
 */

public class TableAttrs
{
	private Map<String, String> mValues = new LinkedHashMap<>();

	public void addAttr(String key, String attr)
	{
		mValues.put(key, attr);
	}

	public String getAttr(String key)
	{
		return mValues.get(key);
	}

	public Set<String> keySet()
	{
		return mValues.keySet();
	}
}
