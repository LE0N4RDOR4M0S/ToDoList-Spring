package com.example.demo.Configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity httpse) throws Exception {
        return httpse
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        // .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        // //--GESTORES
                        // .requestMatchers(HttpMethod.GET,"/gestores").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST,"/gestores/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/gestores/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/gestores/upd/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/gestores/delete/{id}").hasRole("ADMIN")
                        // //--iTINERARIO
                        // .requestMatchers(HttpMethod.GET,"/itinerario").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST,"/itinerario/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/itinerario/rotas/{id}").authenticated()
                        // .requestMatchers(HttpMethod.GET,"/itinerario/parada/{id}").authenticated()
                        // .requestMatchers(HttpMethod.PUT,"/itinerario/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/itinerario/delete/{id}").hasRole("ADMIN")
                        // //--MANUTENCAO
                        // .requestMatchers(HttpMethod.GET,"/manutencao").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/manutencao{placaV}").hasRole("USER")
                        // .requestMatchers(HttpMethod.POST,"/manutencao/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/manutencao/update/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/manutencao/delete/{id}").hasRole("ADMIN")
                        // //--Motorista
                        // .requestMatchers(HttpMethod.GET,"/motorista").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/motorista{cnh}").hasRole("USER")
                        // .requestMatchers(HttpMethod.GET,"/motorista/rotas/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/motorista/turnos/{turno}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST,"/motorista/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/motorista/upd{cnh}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/motorista/delete/{cnh}").hasRole("ADMIN")
                        // //--MULTA
                        // .requestMatchers(HttpMethod.GET,"/multa").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/multa/{placa}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/multa/valor/{valor}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST,"/multa/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/multa/update/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/multa/delete/{id}").hasRole("ADMIN")
                        // //--PARADA
                        // .requestMatchers(HttpMethod.GET,"/paradas").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/paradas/{id}").authenticated()
                        // .requestMatchers(HttpMethod.POST,"/paradas/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/paradas/update/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/paradas/delete/{id}").hasRole("ADMIN")
                        // //--ROTA
                        // .requestMatchers(HttpMethod.GET,"/rotas").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/rotas/{id}").authenticated()
                        // .requestMatchers(HttpMethod.POST,"/rotas/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/rotas/update/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/rotas/delete/{id}").hasRole("ADMIN")
                        // //--VEICULO
                        // .requestMatchers(HttpMethod.GET,"/veiculos").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/veiculos/{placa}").authenticated()
                        // .requestMatchers(HttpMethod.GET,"/veiculos/tipo/{tipo}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/veiculos/acessibilidade").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST,"/veiculos/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/veiculos/update/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/veiculos/delete/{id}").hasRole("ADMIN")
                        // //--ESTUDANTE
                        // .requestMatchers(HttpMethod.GET,"/estudante").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/estudante/{id}").authenticated()
                        // .requestMatchers(HttpMethod.GET,"/estudante/rota/{id}").hasRole("USER")
                        // .requestMatchers(HttpMethod.GET,"/estudante/parada/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/estudante/acessibilidade").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.POST,"/estudante/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.PUT,"/estudante/update/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE,"/estudante/delete/{id}").hasRole("ADMIN")
                        // //--Users
                        // .requestMatchers(HttpMethod.GET,"/user").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/user/add").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/user/delete/{id}").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET,"/dashboard").hasRole("ADMIN")
                        .anyRequest().permitAll()

                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
