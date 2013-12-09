package org.intel.im.analytics.hiveudfs;
import java.io.IOException;

import net.sf.uadetector.service.UADetectorServiceFactory;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;

public class parseUserAgent {
	 public static void main(String[] args) throws IOException {
		 UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
		 String tstStrng = "Mozilla/4.0+(compatible;+MSIE+8.0;+Windows+NT+6.1;+WOW64;+Trident/4.0;+SLCC2;+.NET+CLR+2.0.50727;+.NET+CLR+3.5.30729;+.NET+CLR+3.0.30729;+.NET+CLR+1.1.4322;+.NET4.0C;+.NET4.0E;+MS-RTC+LM+8;+InfoPath.3)".replace('+', ' ');
		 ReadableUserAgent agent = parser.parse(tstStrng);
		 System.out.println(agent.getOperatingSystem().getName());
		 System.out.println(agent.getFamily().getName());
		 System.out.println(agent.getVersionNumber());
		 System.out.println(agent.getProducer());
		 System.out.println(agent.getProducerUrl());
		 System.out.println(agent.getUrl());
		 System.out.println(agent.getDeviceCategory().getName());
	 }

}
