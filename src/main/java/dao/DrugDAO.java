package dao;

import model.Drug;
import java.util.List;

public interface DrugDAO {
    int create(Drug drug);
    boolean update(Drug drug);
    boolean delete(int id);
    Drug findById(int id);
    List<Drug> findAll();
}
