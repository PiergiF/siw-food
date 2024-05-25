package it.uniroma3.siw.siwfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siwfood.model.Image;

public interface ImageRepository extends CrudRepository<Image,Long>{

}

//tutta l'immagine chat gpt
/*
public interface ImageRepository extends JpaRepository<Image,Long>{

}
*/
