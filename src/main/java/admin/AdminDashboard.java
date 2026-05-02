package admin;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
@Component
public class AdminDashboard {
    @PostConstruct
    public void showDashboard() {
        System.out.println("--- Admin Dashboard ---");
        System.out.println("1. View Orders");
        System.out.println("2. Manage Products");
        System.out.println("3. Generate Reports");
        System.out.println("Admin Dashboard is working!");
    }
}
