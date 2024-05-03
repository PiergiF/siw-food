package it.uniroma3.siw.siwfood.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Chef {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String image;
    
    @OneToMany(mappedBy = "chef", fetch=FetchType.EAGER)
    private List<Recipe> recipes;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    
    @Override
    public boolean equals(Object c){
        Chef chef = (Chef)c;
        return this.name.equals(chef.getName()) && this.surname.equals(chef.getSurname()) && this.dateOfBirth.equals(chef.getDateOfBirth());
    }

    @Override
    public int hashCode(){
        return this.name.hashCode() + this.surname.hashCode() + this.dateOfBirth.hashCode();
    }
}
