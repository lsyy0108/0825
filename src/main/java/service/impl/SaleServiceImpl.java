package service.impl;

import dao.SaleDAO;
import dao.impl.SalesDAOImpl;
import model.Sale;
import service.SaleService;

import java.util.List;

public class SaleServiceImpl implements SaleService {

    private SaleDAO saleDAO = new SalesDAOImpl();

    @Override
    public int create(Sale sale) {
        return saleDAO.create(sale);
    }

    @Override
    public boolean update(Sale sale) {
        return saleDAO.update(sale);
    }

    @Override
    public boolean delete(int id) {
        return saleDAO.delete(id);
    }

    @Override
    public List<Sale> findAll() {
        return saleDAO.findAll();
    }

    @Override
    public List<Sale> findByMemberId(int memberId) {
        return saleDAO.findByMemberId(memberId);
    }

    @Override
    public boolean createBatch(List<Sale> sales) {
        boolean allSuccess = true;
        for (Sale s : sales) {
            int id = saleDAO.create(s);
            if (id <= 0) allSuccess = false;
        }
        return allSuccess;
    }
}
