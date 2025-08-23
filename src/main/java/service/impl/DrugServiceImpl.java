package service.impl;

import dao.DrugDAO;
import dao.impl.DrugDAOImpl;
import model.Drug;
import service.DrugService;

import java.util.List;

public class DrugServiceImpl implements DrugService {

    private final DrugDAO drugDAO = new DrugDAOImpl();

    @Override
    public int createDrug(Drug drug) {
        return drugDAO.create(drug);
    }

    @Override
    public boolean updateDrug(Drug drug) {
        return drugDAO.update(drug);
    }

    @Override
    public boolean deleteDrug(int id) {
        return drugDAO.delete(id);
    }

    @Override
    public Drug getDrugById(int id) {
        return drugDAO.findById(id);
    }

    @Override
    public List<Drug> getAllDrugs() {
        return drugDAO.findAll();
    }
}
