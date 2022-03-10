package br.com.compass.pb.shop.quartz;

import br.com.compass.pb.shop.model.Order;
import br.com.compass.pb.shop.model.Status;
import br.com.compass.pb.shop.service.OrderService;
import br.com.compass.pb.shop.util.SendGridAPIUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;
import java.util.List;

public class MonitorOrdersJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try (OrderService os = new OrderService()) {
            List<Order> orders = os.findPendingOrdersByDate(LocalDate.now().minusDays(7L));
            orders.forEach(order -> order.setStatus(Status.CANCELED));

            orders = os.findPendingOrdersByDate(LocalDate.now().minusDays(3L));
            orders.forEach(this::sendWarning);
        }
        catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }


    private void sendWarning(Order order) {
        String subject = "Pedido " + order.getId() + " aguardando pagamento";

        String message = "Olá, " + order.getClient().getName()
                + "O seu pedido será cancelado automaticamente, "
                + "caso o pagamento não seja efetuado dentro dos próximos 4 dias!\n\n"
                + "Dados do pedido:\n" + order;

        SendGridAPIUtil.sendEmail(order.getClient().getEmail(), subject, message);
    }
}
