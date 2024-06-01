package it.uniroma3.siw.siwfood.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Unit;
import it.uniroma3.siw.siwfood.repository.UnitRepository;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public List<String> findAllName(){
        List<Unit> list = unitRepository.findAll(); //solo con JpaRepository
        List<String> allUnitNames = new ArrayList<>();
        for (Unit unit : list) {
            allUnitNames.add(unit.getName());
        }
        return allUnitNames;
    }




    //

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Unit findById(Long id) {
        return unitRepository.findById(id).orElse(null);
    }

    public Optional<Unit> findByName(String name) {
        return unitRepository.findByName(name);//.orElse(null);
    }

    public Unit save(Unit unit) {
        return unitRepository.save(unit);
    }
}

