package it.uniroma3.siw.siwfood.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

//import it.uniroma3.siw.siwfood.controller.validator.ChefValidator;
import it.uniroma3.siw.siwfood.model.Administrator;
import it.uniroma3.siw.siwfood.model.Chef;
import it.uniroma3.siw.siwfood.model.Credentials;
import it.uniroma3.siw.siwfood.model.Customer;
import it.uniroma3.siw.siwfood.service.AdministratorService;
import it.uniroma3.siw.siwfood.service.ChefService;
import it.uniroma3.siw.siwfood.service.CredentialsService;
import it.uniroma3.siw.siwfood.service.CustomerService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class AuthController {

    @Autowired
	private CustomerService customerService;

    @Autowired
	private CredentialsService credentialsService;

    @Autowired
    private ChefService chefService;

    @Autowired
    private AdministratorService administratorService;

    //@Autowired
    //private ChefValidator chefValidator;

    @GetMapping("/loginPage")
    public String getLoginPage() {
        return "loginPage.html";
    }

    @GetMapping(value = "/registrationPage")
    public String getRegistrationPage(Model model) {
		model.addAttribute("credentials", new Credentials());
        return "registrationPage.html";
    }

    @PostMapping("/registrationData")
    public String registerUser(@RequestParam(required = false, name = "name") String name, 
                                @RequestParam(required = false, name = "surname") String surname, 
                                @RequestParam(required = false, name = "email") String email, 
                                @RequestParam(required = false, name = "dateOfBirth") LocalDate dateOfBirth,
                                @RequestParam(required = false, name = "image") MultipartFile file,
                                @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                @RequestParam("role") String role,
                                Model model) {

        //se name è presente nell'invio al controller ma senza valore equivale ad una stringa vuota, se non è proprio presente equivale a NULL
        
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!credentialsBindingResult.hasErrors()) {
            if(role.equals("CUSTOMER")){
                Customer customer = new Customer(name, surname, email, dateOfBirth);
                customerService.saveCustomer(customer);
                credentials.setCustomer(customer);
            }
            else if(role.equals("CHEF")){
                Chef chef = new Chef(name, surname, email, dateOfBirth);

                try {
                byte[] byteFoto = file.getBytes();
                chef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
                chefService.saveChef(chef);
                credentials.setChef(chef);
                } catch (IOException e) {
                    model.addAttribute("message", "Chef upload failed!");
                    return "/registrationPage";
                }

            }
            /*
            else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = new Administrator(name, surname, email, dateOfBirth);
                administratorService.saveAdministrator(administrator);
                credentials.setAdministrator(administrator);
            }
            */
            
            //userService.saveUser(user);
            //credentials.setUser(user);
            credentialsService.saveCredentials(credentials, role);
            //model.addAttribute("RegistrationCompleted", true); //non funziona perché c'è il redirect di mezzo
            return "redirect:/loginPage?registration=true";
        }
        return "registrationPage.html";
    }

    @GetMapping("/admin/creationPage")
    public String getAddChefPage(Model model) {
        model.addAttribute("newCredentials", new Credentials());
        return "admin/creationPage.html";
    }

    @PostMapping("/admin/creationData")
    public String creationUser(@RequestParam(required = false, name = "name") String name, 
                                @RequestParam(required = false, name = "surname") String surname, 
                                @RequestParam(required = false, name = "email") String email, 
                                @RequestParam(required = false, name = "dateOfBirth") LocalDate dateOfBirth,
                                @RequestParam(required = false, name = "image") MultipartFile file,
                                @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                                @RequestParam("role") String role,
                                Model model) {
        //se name è presente nell'invio al controller ma senza valore equivale ad una stringa vuota, se non è proprio presente equivale a NULL
        
        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!credentialsBindingResult.hasErrors()) {
            if(role.equals("CUSTOMER")){
                Customer customer = new Customer(name, surname, email, dateOfBirth);
                customerService.saveCustomer(customer);
                credentials.setCustomer(customer);
            }
            else if(role.equals("CHEF")){
                Chef chef = new Chef(name, surname, email, dateOfBirth);

                try {
                byte[] byteFoto = file.getBytes();
                chef.setImageBase64(Base64.getEncoder().encodeToString(byteFoto));
                chefService.saveChef(chef);
                credentials.setChef(chef);
                } catch (IOException e) {
                    model.addAttribute("message", "Chef upload failed!");
                    return "/admin/creationPage";
                }

            }
            else if(role.equals("ADMINISTRATOR")){
                Administrator administrator = new Administrator(name, surname, email, dateOfBirth);
                administratorService.saveAdministrator(administrator);
                credentials.setAdministrator(administrator);
            }
            
            credentialsService.saveCredentials(credentials, role);
            model.addAttribute("RegistrationCompleted", true); //non funziona perché c'è il redirect di mezzo
            return "redirect:/";
        }
        return "admin/creationPage.html";
    }

}
