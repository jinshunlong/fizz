/**
 * 
 */
package com.runes.xpf.im.core.iq;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:10:01 +0800 (星期六, 17 一月 2009) $
 */
public class AddUserToCompany extends IQ {
	private String companyName;
	private List<String> userNames = new ArrayList<String>();

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<adduser2company xmlns=\"com:runes:adduser2company\"");
		if (companyName != null) {
			buf.append(" name=\"");
			buf.append(companyName);
			buf.append("\"");
		}
		buf.append(">");
		for (int i = 0; i < userNames.size(); i++) {
			buf.append("<user name=\"");
			buf.append(userNames.get(i));
			buf.append("\"/>");
		}
		buf.append("</adduser2company>");

		return buf.toString();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}

}
