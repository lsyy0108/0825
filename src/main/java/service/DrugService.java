package service;

import model.Drug;
import java.util.List;

public interface DrugService {
    int createDrug(Drug drug);
    boolean updateDrug(Drug drug);
    boolean deleteDrug(int id);
    Drug getDrugById(int id);
    List<Drug> getAllDrugs();
}
