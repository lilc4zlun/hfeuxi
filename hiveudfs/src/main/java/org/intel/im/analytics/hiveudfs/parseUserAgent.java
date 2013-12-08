package org.intel.im.analytics.hiveudfs;
import java.io.IOException;

import net.sf.uadetector.service.UADetectorServiceFactory;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;

public class parseUserAgent {
	 public static void main(String[] args) throws IOException {
		 UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
		 ReadableUserAgent agent = parser.parse("Mozilla/5.0 (X11; Linux x86_64; rv:17.0) Gecko/20130917 Firefox/17.0");
		 System.out.println(agent.getOperatingSystem().getName());
		 System.out.println(agent.getFamily().getName());
		 System.out.println(agent.getProducer());
		 System.out.println(agent.getProducerUrl());
		 System.out.println(agent.getType());
		 System.out.println(agent.getTypeName());
		 System.out.println(agent.getUrl());
		 System.out.println(agent.getVersionNumber());
		 System.out.println(agent.getDeviceCategory());
		 
	 }

}
