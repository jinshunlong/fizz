/**
 * 
 */
package com.runes.xpf.im.core.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:10:01 +0800 (星期六, 17 一月 2009) $
 */
public class JabberServerUtil {
	public static interface IRun {
		public Object run(XMPPConnection connection);
	}

	public static Object run(IRun runnable) {
		XMPPConnection connection = getConnection();
		Object obj = null;
		try {
			obj = runnable.run(connection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection.isConnected()) {
				connection.disconnect();
			}
		}
		return obj;
	}

	public static XMPPConnection getConnection() {
		ConnectionConfiguration config = new ConnectionConfiguration(
				"shunlongjin", 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login("admin", "arlongiove");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
