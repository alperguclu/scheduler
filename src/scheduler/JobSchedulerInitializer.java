package scheduler;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import scheduler.model.Job;

@WebListener
public class JobSchedulerInitializer implements ServletContextListener {
	private JobScheduler scheduler;
	
	private EntityManagerFactory emf;
	private EntityManager em ;
	
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	ServletContext ctx = servletContextEvent.getServletContext();
    	emf = Persistence.createEntityManagerFactory("scheduler");
    	em = emf.createEntityManager();
    	
    	if(ctx.getAttribute("JobScheduler")!=null) {
    		scheduler = (JobScheduler) ctx.getAttribute("JobScheduler");
    		scheduler.clean();
    		scheduler.start();
    	}else {
    		scheduler = new JobScheduler(em);
        	scheduler.start();
        	ctx.setAttribute("JobScheduler", scheduler);
    	}

    	List<Job> jobs = em.createNamedQuery("Job.findAll", Job.class).getResultList();
    	
    	for(Job job : jobs) {
    		scheduler.addJob(job);
    	}
    	
    	System.out.println("Context Initialized. Context Path: "+ctx.getContextPath());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	em.clear();
    	em.close();
    	emf.close();
    	scheduler.clean();

    	System.out.println("Context Destroyed.");
    }
	
}