package com.pao.proiect.elearning.repository;

import java.util.List;
import java.util.Optional;

//interfata generica pentru operatiile standard de tip CRUD pe baza de date
public interface Repository<T, ID> {

    //este salvata o entitate noua in baza de date
    void save(T entity);

    //este cautata o entitate dupa identificatorul ei unic, incapsulata in Optional pentru a evita null
    Optional<T> findById(ID id);

    //sunt preluate toate inregistrarile din tabelul corespunzator entitatii
    List<T> findAll();

    //sunt actualizate datele unei entitati deja existente
    void update(T entity);

    //este stearsa o inregistrare pe baza identificatorului
    void delete(ID id);
}