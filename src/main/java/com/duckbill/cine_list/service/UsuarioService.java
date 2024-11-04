package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo para criar um novo usuário com data de criação
    public Usuario create(Usuario usuario) {
        usuario.setId(UUID.randomUUID().toString()); // Define um UUID em formato String
        if (!isValidCPF(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> getById(UUID id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario update(UUID id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioDetails.getNome());
                    usuario.setEmail(usuarioDetails.getEmail());
                    usuario.setSenha(usuarioDetails.getSenha());
                    usuario.setCpf(usuarioDetails.getCpf());
                    if (!isValidCPF(usuario.getCpf())) {
                        throw new IllegalArgumentException("CPF inválido");
                    }
                    usuario.setUpdatedAt(LocalDateTime.now());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void delete(UUID id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setDeletedAt(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }

    // Validação de CPF simplificada
    private boolean isValidCPF(String cpf) {
        // Remover pontuações ('.', '-', etc.)
        String cpfClean = cpf.replaceAll("\\D", "");

        // Verificar se tem 11 dígitos
        if (cpfClean.length() != 11) return false;

        // Verificar se todos os dígitos são iguais (ex.: 111.111.111-11)
        if (cpfClean.chars().distinct().count() == 1) return false;

        // Calcular os dígitos verificadores
        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigito(cpfClean.substring(0, 9), pesos1);
        int digito2 = calcularDigito(cpfClean.substring(0, 9) + digito1, pesos2);

        // Verificar se os dígitos calculados são iguais aos fornecidos
        return cpfClean.equals(cpfClean.substring(0, 9) + digito1 + digito2);
    }

    private int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * pesos[i];
        }
        int resto = 11 - (soma % 11);
        return (resto > 9) ? 0 : resto;
    }
}
