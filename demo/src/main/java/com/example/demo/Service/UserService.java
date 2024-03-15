package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Função para criar um novo usuário
     *
     * @param user O novo usuário a ser criado
     * @return O usuário criado
     */
    public User createUser(User user) {
        // Criptografar a senha antes de salvar no banco de dados
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Função para buscar um usuário pelo nome de usuário
     *
     * @param username O nome de usuário do usuário
     * @return O usuário encontrado
     */
    public User getUserByUsername(String username) {
        return userRepository.findByName(username).orElse(null);
    }

    /**
     * Função para verificar se a senha fornecida corresponde à senha do usuário
     *
     * @param user     O usuário para o qual verificar a senha
     * @param password A senha fornecida para verificar
     * @return true se a senha corresponder, false caso contrário
     */
    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }
}
