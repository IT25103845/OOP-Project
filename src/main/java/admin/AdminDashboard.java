package admin;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class AdminDashboard {

    private ArrayList<Double> orders = new ArrayList<>();

    @PostConstruct
    public void showDashboard() {

        // sample data (temporary)
        orders.add(2500.0);
        orders.add(1500.0);
        orders.add(3000.0);

        System.out.println("--- Admin Dashboard ---");

        System.out.println("Total Orders: " + getTotalOrders());
        System.out.println("Total Revenue: " + getTotalRevenue());
    }

    public int getTotalOrders() {
        return orders.size();
    }

    public double getTotalRevenue() {
        double total = 0;
        for (double amount : orders) {
            total += amount;
        }
        return total;
    }
}