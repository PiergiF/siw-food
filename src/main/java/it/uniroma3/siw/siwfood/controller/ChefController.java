package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.siwfood.controller.validator.ChefValidator;
import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Image;
import it.uniroma3.siw.siwfood.repository.ChefRepository;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.FileUploadUtil;
import it.uniroma3.siw.siwfood.service.ImageService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.IOException;



@Controller
public class ChefController {

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private ChefService chefService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ChefValidator chefValidator;

    @GetMapping(value = "/admin/allChefsPage")
    public String getAllChefsPage(Model model){
        model.addAttribute("chefs", this.chefRepository.findAll());
        return "admin/allChefsPage.html";
    }

    @GetMapping("/chefPage/{id}")
	public String getChefPage(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefRepository.findById(id).get());
		return "chefPage.html";
	}

    /*
    //pre immagine
    @GetMapping("/admin/addChefPage")
    public String getAddChefPage(Model model) {
        model.addAttribute("newChef", new Chef());
        //model.addAttribute("newImage", new Image());
        return "admin/addChefPage.html";
    }
    */
    

    
    @GetMapping("/admin/addChefPage")
    public String getAddChefPage(Model model) {
        model.addAttribute("newChef", new Chef());
        //model.addAttribute("newImage", new Image());
        return "admin/addChefPage.html";
    }


    
    //tutta l'immagine (mio)
    @PostMapping("/admin/addChefData")
    public String newChef(HttpServletBean request, @Valid @ModelAttribute ("newChef") Chef chef, BindingResult chefBindingResult,
                            @Valid @RequestParam("image") MultipartFile file, BindingResult imageBindingResult,
                            Model model) throws IOException, SerialException, SQLException{
        this.chefValidator.validate(chef, chefBindingResult);
        if(!chefBindingResult.hasErrors()){ //&& !imageBindingResult.hasErrors()){
            byte[] bytes = file.getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

            Image image = new Image();
            image.setImage(blob);
            imageService.create(image);
            return "chefPage.html";
            //model.addAttribute("chef",this.chefRepository.findById(chef.getId()).get())
            //return "redirect:/chefPage.html/"+ chef.getId() ;
        }else{
            return "admin/addChefPage.html";
        }

    }
    
    /*
    //vecchio tutta l'immagine
    @PostMapping("/admin/addChefData")
    public String newChef(@Valid @ModelAttribute ("newChef") Chef chef, BindingResult chefBindingResult,
                            @Valid @RequestParam("image") MultipartFile file, BindingResult imageBindingResult,
                            Model model) {
        this.chefValidator.validate(chef, chefBindingResult);
        if(!chefBindingResult.hasErrors() && !imageBindingResult.hasErrors()){
            try{
                chefService.saveChefWithImage(chef, file);
                model.addAttribute("chef", chef);
                //this.chefRepository.save(chef);
                return "chefPage.html";
            } catch(IOException e){
                model.addAttribute("failImage", true);
                return "admin/addChefPage.html";
            }
        }else{
            return "admin/addChefPage.html";
        }
    }
    */
    
    /*
    //Sito
    @PostMapping("/admin/addChefData")
    public String newChef(@Valid @ModelAttribute ("newChef") Chef chef, BindingResult chefBindingResult,
                            @Valid @RequestParam("image") MultipartFile file, BindingResult imageBindingResult,
                            Model model) throws IOException{
        this.chefValidator.validate(chef, chefBindingResult);
        if(!chefBindingResult.hasErrors()){ //&& !imageBindingResult.hasErrors()){

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            chef.setImage(fileName);
            
            this.chefRepository.save(chef);
    
            String uploadDir = "image/chefImage/" + chef.getId();
    
            FileUploadUtil.saveFile(uploadDir, fileName, file);
            return "chefPage.html";
        }else{
            return "admin/addChefPage.html";
        }
    }
    */

    /*
    //tutta l'immagine
    @PostMapping("/admin/addChefData")
    public String newChef(@Valid @ModelAttribute ("newChef") Chef chef, BindingResult chefBindingResult,
                            @Valid @RequestParam("image") MultipartFile file, BindingResult imageBindingResult,
                            Model model) throws IOException{
        this.chefValidator.validate(chef, chefBindingResult);
        if(!chefBindingResult.hasErrors()){ //&& !imageBindingResult.hasErrors()){
            try{
                chefService.saveChefWithImage(chef, file);
                model.addAttribute("chef", chef);
                return "chefPage.html";
            } catch(IOException e){
                model.addAttribute("failImage", true);
                return "admin/addChefPage.html";
            }
                
        }else{
            return "admin/addChefPage.html";
        }
    }
    */
}

