/**
 * 
 */
package com.runes.xpf.im.server.plugin.iq;

import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.handler.IQHandler;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:13:49 +0800 (星期六, 17 一月 2009) $
 */
public abstract class XpfIQHandler extends IQHandler {

	protected IQHandlerInfo info;

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

	public XpfIQHandler(String moduleName) {
		super(moduleName);
		info = new IQHandlerInfo(getInfoName(), getInfoSpace());
	}

	protected abstract String getInfoSpace();

	protected abstract String getInfoName();

}
