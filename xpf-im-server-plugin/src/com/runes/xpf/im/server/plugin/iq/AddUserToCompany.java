package com.runes.xpf.im.server.plugin.iq;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

public class AddUserToCompany extends XpfIQHandler {
	private static final String MODULE_NAME = "xpf user2company handler";

	private static final String INFO_SPACE = "com:runes:adduser2company";
	private static final String INFO_NAME = "adduser2company";
	String domain;

	public AddUserToCompany() {
		super(MODULE_NAME);
		domain = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
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
		String groupName = group.attributeValue("name");
		List<Element> users = group.elements();
		List<String> userNames = new ArrayList<String>();
		for (int i = 0; i < users.size(); i++) {
			Element ele = users.get(i);
			if (ele.attributeValue("name") != null) {
				userNames.add(ele.attributeValue("name"));
			}
		}
		GroupManager manager = GroupManager.getInstance();
		Group openFireGroup = null;
		try {
			openFireGroup = manager.getGroup(groupName);
		} catch (GroupNotFoundException e1) {
			e1.printStackTrace();
		}
		try {

			if (openFireGroup == null) {
				openFireGroup = GroupManager.getInstance().createGroup(
						groupName);
				openFireGroup.setDescription(groupName);
				openFireGroup.getProperties().put("sharedRoster.showInRoster",
						"onlyGroup");
				openFireGroup.getProperties().put("sharedRoster.displayName",
						groupName);
			}
			if (userNames.size() != 0) {
				for (String userName : userNames) {
					String jid = userName + "@" + domain;
					if (!openFireGroup.getMembers().contains(new JID(jid))) {
						openFireGroup.getMembers().add(new JID(jid));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			reply.setChildElement(group.createCopy());
			reply.setError(PacketError.Condition.bad_request);
			return reply;

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
