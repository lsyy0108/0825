package dao;

import model.Sale;
import java.util.List;
import java.util.Optional;

public interface SaleDAO {
    int create(Sale sale);
    boolean update(Sale sale);
    boolean delete(int id);
    boolean deleteByOrderId(String orderId);
    List<Sale> findAll();
    List<Sale> findByMemberId(int memberId);
    Optional<Sale> findById(int id);
    List<Sale> findByOrderId(String orderId);
}
