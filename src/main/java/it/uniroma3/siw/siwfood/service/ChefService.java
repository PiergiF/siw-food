package it.uniroma3.siw.siwfood.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.siwfood.model.Chef;
//import it.uniroma3.siwfood.model.Image;
import it.uniroma3.siw.siwfood.repository.ChefRepository;
//import it.uniroma3.siwfood.repository.ImageRepository;

@Service
public class ChefService {
    
    //sito
    @Autowired
    private ChefRepository chefRepository;

    public boolean existsByNameAndSurname(String name, String surname){
        return chefRepository.existsByNameAndSurname(name, surname);
    }

    /*
    //tutta l'immagine
    @Autowired
    private ImageRepository imageRepository;
    
    public void saveChefWithImage(Chef chef, MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        image.setData(file.getBytes());
        imageRepository.save(image);

        chef.setImage(image);
        this.chefRepository.save(chef);
    }
    */
    
    
}
