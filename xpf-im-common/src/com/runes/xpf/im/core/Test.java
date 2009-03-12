/**
 * 
 */
package com.runes.xpf.im.core;

/**
 * @author jinshunlong
 * @created Jan 16, 2009
 * @doc 描述
 * @version $Reversion$ $Date: 2009-01-17 10:10:01 +0800 (星期六, 17 一月 2009) $
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IMContext context = new IMContext();
//		context.addUser("曲涛2", "伟通鼎汇", "123");
		 context.getOfflineMessages("曲涛2","伟通鼎汇 ");
	}

}
