package it.uniroma3.siw.siwfood.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

import static it.uniroma3.siw.siwfood.model.Credentials.CUSTOMER_ROLE;
import static it.uniroma3.siw.siwfood.model.Credentials.CHEF_ROLE;
import static it.uniroma3.siw.siwfood.model.Credentials.ADMINISTRATOR_ROLE;

@Configuration
@EnableWebSecurity
//public  class WebSecurityConfig {
	public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().and().cors().disable()
                .authorizeHttpRequests()
//                .requestMatchers("/**").permitAll()
                // chiunque (autenticato o no) può accedere alle pagine index, login, register, ai css e alle immagini
                .requestMatchers(HttpMethod.GET,"/", "/loginPage", "/oauth2/**", "/registrationPage", "/search", "/all/**", "/messages/**", "/css/**", "/javascript/**", "/images/**", "favicon.ico", "/rest/**").permitAll()
        		// chiunque (autenticato o no) può mandare richieste POST al punto di accesso per login e register 
                //.requestMatchers(HttpMethod.POST,"/settingsData").hasAnyAuthority(CUSTOMER_ROLE, CHEF_ROLE, ADMINISTRATOR_ROLE)
                .requestMatchers(HttpMethod.POST,"/registrationData","/loginPage").permitAll()
                .requestMatchers(HttpMethod.GET,"/chef_admin/**").hasAnyAuthority(CHEF_ROLE, ADMINISTRATOR_ROLE)
                .requestMatchers(HttpMethod.POST,"/chef_admin/**").hasAnyAuthority(CHEF_ROLE, ADMINISTRATOR_ROLE)
                .requestMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMINISTRATOR_ROLE)
                .requestMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority(ADMINISTRATOR_ROLE)
        		// tutti gli utenti autenticati possono accere alle pagine rimanenti 
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .loginPage("/loginPage")
                .defaultSuccessUrl("/", true)
                .failureUrl("/loginPage?error=true")

                // LOGIN: qui definiamo il login
                .and().formLogin()
                .loginPage("/loginPage")
                .permitAll()
                .defaultSuccessUrl("/", true)
                .failureUrl("/loginPage?error=true")
                // LOGOUT: qui definiamo il logout
                .and()
                .logout()
                // il logout è attivato con una richiesta GET a "/logout"
                .logoutUrl("/logout")
                // in caso di successo, si viene reindirizzati alla home
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true).permitAll();
        return httpSecurity.build();
    }
}
