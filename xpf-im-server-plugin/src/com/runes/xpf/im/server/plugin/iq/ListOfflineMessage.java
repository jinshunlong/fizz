/**
 * 
 */
package com.runes.xpf.im.server.plugin.iq;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jivesoftware.openfire.OfflineMessage;
import org.jivesoftware.openfire.OfflineMessageStore;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:13:49 +0800 (星期六, 17 一月 2009) $
 */
public class ListOfflineMessage extends XpfIQHandler {
	private static final String MODULE_NAME = "xpf msg handler";

	private static final String INFO_SPACE = "com:runes:messages";
	private static final String INFO_NAME = "messages";

	public ListOfflineMessage() {
		super(MODULE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.runes.xpf.im.server.plugin.iq.XpfIQHandler#getInfoName()
	 */
	@Override
	protected String getInfoName() {
		return INFO_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.runes.xpf.im.server.plugin.iq.XpfIQHandler#getInfoSpace()
	 */
	@Override
	protected String getInfoSpace() {
		return INFO_SPACE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.openfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
	 */
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = IQ.createResultIQ(packet);
		Element user = packet.getChildElement();// 1

		if (!IQ.Type.set.equals(packet.getType())) {
			System.out.println("非法的请求类型");
			reply.setChildElement(user.createCopy());
			reply.setError(PacketError.Condition.bad_request);
			return reply;
		}
		String name = user.attributeValue("name");
		try {
			OfflineMessageStore of = OfflineMessageStore.getInstance();
			Collection<OfflineMessage> offlineMsgs = of
					.getMessages(name, false);
			Iterator<OfflineMessage> msgs = offlineMsgs.iterator();
			Element msgEles = reply.setChildElement("messages",
					"com:runes:messages");
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyy.MM.dd HH:mm:ss");
			while (msgs.hasNext()) {
				OfflineMessage msg = msgs.next();
				String content = msg.getBody();
				String fromId = msg.getFrom().getNode();
				Date date = msg.getCreationDate();

				String dateStr = format.format(date);
				Element msgEle = msgEles.addElement("message");
				msgEle.addElement("content").setText(content);
				msgEle.addElement("fromId").setText(fromId);
				msgEle.addElement("date").setText(dateStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reply.setChildElement(user.createCopy());
			reply.setError(PacketError.Condition.bad_request);
			return reply;

		}

		System.out.println("OffLineMsg XML:" + reply.toXML());

		return reply;
	}

}
