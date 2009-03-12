/**
 * 
 */
package com.runes.xpf.im.server.plugin.iq;

import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:13:49 +0800 (星期六, 17 一月 2009) $
 */
public class CreateCompanyGroup extends XpfIQHandler {
	private static final String MODULE_NAME = "xpf company handler";

	private static final String INFO_SPACE = "com:runes:company";
	private static final String INFO_NAME = "company";

	public CreateCompanyGroup() {
		super(MODULE_NAME);
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = IQ.createResultIQ(packet);
		Element group = packet.getChildElement();// 1

		if (!IQ.Type.set.equals(packet.getType())) {
			System.out.println("非法的请求类型");
			reply.setChildElement(group.createCopy());
			reply.setError(PacketError.Condition.bad_request);
			return reply;
		}
		String name = group.attributeValue("name");
		boolean has = false;
		try {
			GroupManager.getInstance().getGroup(name);
			has = true;
		} catch (GroupNotFoundException e1) {
		}
		if (!has) {
			try {
				Group openFireGroup = GroupManager.getInstance().createGroup(
						name);
				openFireGroup.setDescription(name);
				openFireGroup.getProperties().put("sharedRoster.showInRoster",
						"onlyGroup");
				openFireGroup.getProperties().put("sharedRoster.displayName",
						name);
			} catch (GroupAlreadyExistsException e) {
				e.printStackTrace();
				reply.setChildElement(group.createCopy());
				reply.setError(PacketError.Condition.bad_request);
				return reply;

			}
		}
		reply.setChildElement(group.createCopy());// 2

		System.out.println("返回的最终XML" + reply.toXML());

		return reply;
	}

	@Override
	protected String getInfoName() {
		return INFO_NAME;
	}

	@Override
	protected String getInfoSpace() {
		return INFO_SPACE;
	}

}
