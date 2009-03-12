/**
 * 
 */
package com.runes.xpf.im.core;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import com.runes.xpf.im.core.iq.AddUserToCompany;
import com.runes.xpf.im.core.iq.CreateCompanyGroup;
import com.runes.xpf.im.core.iq.ListOfflineMessage;
import com.runes.xpf.im.core.model.OneOfflineMessage;
import com.runes.xpf.im.core.util.JabberServerUtil;
import com.runes.xpf.im.core.util.JabberServerUtil.IRun;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:10:01 +0800 (星期六, 17 一月 2009) $
 */
public class IMContext {
	public void createGroupForCompamy(final String compName) {
		JabberServerUtil.run(new IRun() {

			public Object run(XMPPConnection connection) {
				CreateCompanyGroup reg = new CreateCompanyGroup();
				reg.setType(IQ.Type.SET);
				reg.setTo(connection.getServiceName());
				reg.setName(compName);
				PacketFilter filter = new AndFilter(new PacketIDFilter(reg
						.getPacketID()), new PacketTypeFilter(IQ.class));
				PacketCollector collector = connection
						.createPacketCollector(filter);
				connection.sendPacket(reg);
				IQ result = (IQ) collector.nextResult(SmackConfiguration
						.getPacketReplyTimeout());
				// Stop queuing results
				collector.cancel();
				if (result == null) {
					System.out.println("No response from server.");
				} else if (result.getType() == IQ.Type.ERROR) {
					System.out.println(result.getError());
				}
				return null;
			}

		});

	}

	public ListOfflineMessage getOfflineMessages(final String _userName,
			String companyName) {
		final String userName = _userName + "-" + companyName;
		ListOfflineMessage info = (ListOfflineMessage) JabberServerUtil
				.run(new IRun() {

					public Object run(XMPPConnection connection) {
						ListOfflineMessage info = new ListOfflineMessage();
						info.setType(IQ.Type.SET);
						info.setUserName(userName);
						PacketFilter filter = new AndFilter(new PacketIDFilter(
								info.getPacketID()), new PacketTypeFilter(
								IQ.class));
						PacketCollector collector = connection
								.createPacketCollector(filter);

						connection.sendPacket(info);

						// Wait up to 5 seconds for a result.
						IQ result = (IQ) collector
								.nextResult(SmackConfiguration
										.getPacketReplyTimeout());
						// Stop queuing results
						collector.cancel();
						if (result == null) {
							System.out.println("No response from the server.");
							return info;
						}
						if (result.getType() == IQ.Type.ERROR) {
							System.out.println(result.getError());
							return info;
						}
						System.out.println("size ="
								+ ((ListOfflineMessage) result).getMessages()
										.size());
						ListOfflineMessage l = (ListOfflineMessage) result;
						for (OneOfflineMessage one : l.getMessages()) {
							System.out.println(one.getContent());
							System.out.println(one.getDate().toString());
							
						}
						return info;
					}
				});
		return info;
	}

	public void addUser(String userName, final String compName, final String pwd) {
		final String imUserName = userName + "-" + compName;
		JabberServerUtil.run(new IRun() {

			public Object run(XMPPConnection connection) {
				try {
					connection.getAccountManager().createAccount(imUserName,
							pwd);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				AddUserToCompany reg = new AddUserToCompany();
				reg.setType(IQ.Type.SET);
				reg.setTo(connection.getServiceName());
				reg.setCompanyName(compName);
				reg.getUserNames().add(imUserName);
				PacketFilter filter = new AndFilter(new PacketIDFilter(reg
						.getPacketID()), new PacketTypeFilter(IQ.class));
				PacketCollector collector = connection
						.createPacketCollector(filter);
				connection.sendPacket(reg);
				IQ result = (IQ) collector.nextResult(SmackConfiguration
						.getPacketReplyTimeout());
				// Stop queuing results
				collector.cancel();
				if (result == null) {
					System.out.println("No response from server.");
				} else if (result.getType() == IQ.Type.ERROR) {
					System.out.println(result.getError());
				}
				return null;
			}
		});

	}
}
