package br.com.compass.pb.shop.quartz;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzJobListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JobDetail job = JobBuilder.newJob(MonitorOrdersJob.class).build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("CroneTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 1/1 * ? *"))
                .build();

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }
        catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}


//            Scheduler scheduler = ((SchedulerFactory) sce.getServletContext()
//                        .getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY))
//                        .getScheduler();

//                    .withIdentity("SimpleTrigger")
//                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(5))
//            .build();
