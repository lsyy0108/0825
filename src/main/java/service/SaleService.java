package service;

import model.Sale;
import java.util.List;

public interface SaleService {
    int create(Sale sale); // 單筆
    boolean update(Sale sale);
    boolean delete(int id);
    List<Sale> findAll();
    List<Sale> findByMemberId(int memberId);
    
    // 新增：批次建立多筆訂單
    boolean createBatch(List<Sale> sales);
}
