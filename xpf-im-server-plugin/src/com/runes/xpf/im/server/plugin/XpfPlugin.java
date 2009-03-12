/**
 * 
 */
package com.runes.xpf.im.server.plugin;

import java.io.File;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

import com.runes.xpf.im.server.plugin.iq.AddUserToCompany;
import com.runes.xpf.im.server.plugin.iq.CreateCompanyGroup;
import com.runes.xpf.im.server.plugin.iq.ListOfflineMessage;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:13:49 +0800 (星期六, 17 一月 2009) $
 */
public class XpfPlugin implements Plugin {
	private XMPPServer server;

	public void destroyPlugin() {

	}

	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		System.out.println("--XPF IQ---");
		server = XMPPServer.getInstance();
		server.getIQRouter().addHandler(new CreateCompanyGroup()); // 1

		server.getIQRouter().addHandler(new AddUserToCompany());
		server.getIQRouter().addHandler(new ListOfflineMessage());

	}

}
