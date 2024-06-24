package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

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
import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.repository.ChefRepository;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.FileUploadUtil;
import it.uniroma3.siw.siwfood.service.RecipeService;
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
    private ChefValidator chefValidator;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping(value = "/all/allChefsPage")
    public String getAllChefsPage(Model model){
        model.addAttribute("chefs", this.chefRepository.findAll());
        return "all/allChefsPage.html";
    }

    @GetMapping("/all/chefPage/{id}")
	public String getChefPage(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefRepository.findById(id).get());
		return "all/chefPage.html";
	}
    

    @GetMapping("/admin/editChefPage/{id}")
    public String getEditChefPage(@PathVariable("id") Long chefId, Model model) {
        model.addAttribute("chef",chefService.findById(chefId));
        return "admin/editChefPage.html";
    }

    @PostMapping("/admin/editChefData/{id}")
    public String postMethodName(@PathVariable("id") Long id, @Valid @ModelAttribute("chef") Chef chef, BindingResult chefBindingResult,
                                    @RequestParam("removeImage") String remove,
                                    @RequestParam(required = false, value = "image") MultipartFile file, Model model) {
        
        if (chefBindingResult.hasErrors()) {
            return "/admin/editChefPage/" + id;
        }

        Chef existingChef = chefService.findById(id);
        existingChef.setName(chef.getName());
        existingChef.setSurname(chef.getSurname());
        existingChef.setDateOfBirth(chef.getDateOfBirth());
        existingChef.setEmail(chef.getEmail());

        if(remove.equals("true")){
            try {
                byte[] byteFoto = file.getBytes();
                existingChef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
            }catch (IOException e) {
                model.addAttribute("message", "Chef upload failed!");
                return "admin/editChefPage.html";
            }
        }

        chefService.saveChef(existingChef);
        
        return "redirect:/all/chefPage/" + existingChef.getId();
    }
    
    
    
    @GetMapping("/admin/removeCredentialsChef/{id}")
    public String removeCredentialsChef(@PathVariable("id") Long chefId) {
        
        credentialsService.deleteByChefId(chefId);

        return "redirect:/all/allChefsPage";
    }

    @GetMapping("/admin/removeTotalChef/{id}")
    public String removeTotalChef(@PathVariable("id") Long chefId) {

        credentialsService.deleteByChefId(chefId);
        recipeService.deleteRecipesByChefId(chefId);
        chefService.deleteById(chefId);

        return "redirect:/all/allChefsPage";
    }
    
    

    /*
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
    */
    
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

