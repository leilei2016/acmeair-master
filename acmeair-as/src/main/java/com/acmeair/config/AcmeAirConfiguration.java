package com.acmeair.config;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.acmeair.service.AuthService;
import com.acmeair.service.ServiceLocator;


@Path("/login/config")
public class AcmeAirConfiguration {
    
	@Inject
	BeanManager beanManager;
	Logger logger = Logger.getLogger(AcmeAirConfiguration.class.getName());

	private AuthService authService = ServiceLocator.instance().getService(AuthService.class);
		
    public AcmeAirConfiguration() {
        super();
    }

	@PostConstruct
	private void initialization()  {		
		if(beanManager == null){
			logger.info("Attempting to look up BeanManager through JNDI at java:comp/BeanManager");
			try {
				beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
			} catch (NamingException e) {
				logger.severe("BeanManager not found at java:comp/BeanManager");
			}
		}
		
		if(beanManager == null){
			logger.info("Attempting to look up BeanManager through JNDI at java:comp/env/BeanManager");
			try {
				beanManager = (BeanManager) new InitialContext().lookup("java:comp/env/BeanManager");
			} catch (NamingException e) {
				logger.severe("BeanManager not found at java:comp/env/BeanManager ");
			}
		}
	}
   			
	@GET
	@Path("/countSessions")
	@Produces("application/json")
	public Response countCustomerSessions() {
		try {
			Long customerCount = authService.countSessions();
			
			return Response.ok(customerCount).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.ok(-1).build();
		}
	}
			
}
