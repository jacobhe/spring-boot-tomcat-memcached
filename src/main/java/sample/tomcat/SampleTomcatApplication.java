/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.tomcat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.catalina.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import de.javakaffee.web.msm.MemcachedBackupSessionManager;

@SpringBootApplication
public class SampleTomcatApplication {

	private static Log logger = LogFactory.getLog(SampleTomcatApplication.class);

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				logger.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}

		};
	}
	
	@Bean
	public EmbeddedServletContainerFactory tomcat() {
	    return new TomcatEmbeddedServletContainerFactory() {

	        @Override
	        protected void postProcessContext(Context context) {
	            MemcachedBackupSessionManager manager = new MemcachedBackupSessionManager();
	            manager.setMemcachedNodes("n1:192.168.1.216:11211");
	            manager.setRequestUriIgnorePattern(".*\\.(ico|png|gif|jpg|css|js)$");
	            manager.setSticky(false);
	            context.setManager(manager);
	        }

	    };
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleTomcatApplication.class, args);
	}

}
