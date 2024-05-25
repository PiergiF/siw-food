package it.uniroma3.siw.siwfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siwfood.model.Image;
import it.uniroma3.siw.siwfood.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    
    public Image create(Image image){
        return imageRepository.save(image);
    }

    public List<Image> viewAll() {
        return (List<Image>) imageRepository.findAll();
    }

    public Image viewById(long id) {
        return imageRepository.findById(id).get();
    }
}
