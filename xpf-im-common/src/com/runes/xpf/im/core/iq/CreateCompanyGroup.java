/**
 * 
 */
package com.runes.xpf.im.core.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:10:01 +0800 (星期六, 17 一月 2009) $
 */
public class CreateCompanyGroup extends IQ {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<company xmlns=\"com:runes:company\"");
		if (name != null) {
			buf.append(" name=\"");
			buf.append(name);
			buf.append("\"");
		}
		buf.append("/>");
		return buf.toString();
	}

}
