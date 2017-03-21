/**
 * de.hjbflyer.connexion3dapi
 *
 * Activator.java
 *
 * Copyright(c) user 2017
 */
package osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
//CHECKSTYLE:OFF
/**
 * @author author
 * 
 *         Copyright(c) author 2017
 *
 */
public class Activator implements BundleActivator {

	private BundleContext m_context;

	public BundleContext getContext() {
		return m_context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		m_context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		m_context = null;
	}
}
