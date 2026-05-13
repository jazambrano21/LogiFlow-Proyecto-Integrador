package com.logiflow.auth;

import com.logiflow.auth.model.Rol;
import com.logiflow.auth.model.Usuario;
import com.logiflow.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAuthApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(Usuario.builder()
                        .nombre("Admin")
                        .apellido("LogiFlow")
                        .email("admin@logiflow.com")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .rol(Rol.ADMIN)
                        .build());

                usuarioRepository.save(Usuario.builder()
                        .nombre("Operador")
                        .apellido("LogiFlow")
                        .email("operador@logiflow.com")
                        .passwordHash(passwordEncoder.encode("operador123"))
                        .rol(Rol.OPERADOR)
                        .build());
            }
        };
    }
}
