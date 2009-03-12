/**
 * 
 */
package com.runes.xpf.im.core.iq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.runes.xpf.im.core.model.OneOfflineMessage;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:10:01 +0800 (星期六, 17 一月 2009) $
 */
public class ListOfflineMessage extends IQ {
	String userName;
	List<OneOfflineMessage> messages = new ArrayList<OneOfflineMessage>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<messages xmlns=\"com:runes:messages\"");
		if (userName != null) {
			buf.append(" name=\"");
			buf.append(userName);
			buf.append("\"");
		}
		buf.append(">");
		buf.append("</messages>");

		return buf.toString();
	}

	public static class Provider implements IQProvider {

		/**
		 * Provider Constructor.
		 */
		public Provider() {
			super();
		}

		public IQ parseIQ(XmlPullParser parser) throws Exception {
			ListOfflineMessage msgList = new ListOfflineMessage();
			SimpleDateFormat sf = new SimpleDateFormat();
			sf.applyPattern("yyyy.MM.dd HH:mm:ss");
			boolean done = false;
			while (!done) {
				int eventType = parser.next();
				if (eventType == XmlPullParser.START_TAG
						&& parser.getName().equals("message")) {
					OneOfflineMessage msg = new OneOfflineMessage();
					boolean oneDone = false;
					while (!oneDone) {
						int oneEventType = parser.next();
						if (oneEventType == XmlPullParser.END_TAG) {
							if (parser.getName().equals("message")) {
								msgList.getMessages().add(msg);
								oneDone = true;
							}
						} else if (oneEventType == XmlPullParser.START_TAG
								&& parser.getName().equals("content")) {
							msg.setContent(parser.nextText());
						} else if (oneEventType == XmlPullParser.START_TAG
								&& parser.getName().equals("fromId")) {
							msg.setFrom(parser.nextText());
						} else if (oneEventType == XmlPullParser.START_TAG
								&& parser.getName().equals("date")) {
							msg.setDate(sf.parse(parser.nextText()));
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (parser.getName().equals("messages")) {
						done = true;
					}
				}
			}
			return msgList;
		}
	}

	public List<OneOfflineMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<OneOfflineMessage> messages) {
		this.messages = messages;
	}

}
