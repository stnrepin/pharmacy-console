import models.Disease;
import models.Medicine;
import models.MedicineOrder;
import services.impl.PharmacyServiceImpl;

public class Main {
    public static void main(String[] args) {
        var service = new PharmacyServiceImpl();

        var dis = new Disease("d");
        var med1 = new Medicine("1", 5, 5);
        var med2 = new Medicine("2", 10, 2);

        med1.addTargetDisease(dis);

        service.addMedicine(med1);
        service.addMedicine(med2);
        service.orderMedicine(med1.getId(), 2);
        service.orderMedicine(med2.getId(), 1);

        System.out.println("Medicine count: "+ service.getAllMedicines().size());
        System.out.println("Orders cost: " + service.getOrderTotalCostMonth());
    }
}
