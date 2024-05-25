package it.uniroma3.siw.siwfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Amount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int Amount;
    private UnitOfMeasurament unit;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getAmount() {
        return Amount;
    }
    public void setAmount(int amount) {
        Amount = amount;
    }
    public UnitOfMeasurament getUnit() {
        return unit;
    }
    public void setUnit(UnitOfMeasurament unit) {
        this.unit = unit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Amount;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Amount other = (Amount) obj;
        if (Amount != other.Amount)
            return false;
        if (unit != other.unit)
            return false;
        return true;
    }
    
}
