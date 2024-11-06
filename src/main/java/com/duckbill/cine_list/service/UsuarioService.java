package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo para criar um novo usuário com data de criação
    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario.setId(UUID.randomUUID().toString());

        if (!isValidCPF(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toDto(savedUsuario);
    }

    // Metodo para buscar um usuário pelo ID
    public Optional<UsuarioDTO> getById(UUID id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toDto);
    }

    // Metodo para listar todos os usuários
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    // Metodo para atualizar um usuário existente
    public UsuarioDTO update(String id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(UUID.fromString(id))
                .map(usuario -> {
                    usuario.setNome(usuarioDTO.getNome());
                    usuario.setEmail(usuarioDTO.getEmail());
                    usuario.setCpf(usuarioDTO.getCpf());

                    if (!isValidCPF(usuario.getCpf())) {
                        throw new IllegalArgumentException("CPF inválido");
                    }

                    usuario.setUpdatedAt(LocalDateTime.now());
                    Usuario updatedUsuario = usuarioRepository.save(usuario);
                    return UsuarioMapper.toDto(updatedUsuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Metodo para deletar logicamente um usuário
    public void delete(UUID id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setDeletedAt(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }

    // Validação de CPF simplificada
    private boolean isValidCPF(String cpf) {
        String cpfClean = cpf.replaceAll("\\D", "");

        if (cpfClean.length() != 11) return false;
        if (cpfClean.chars().distinct().count() == 1) return false;

        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigito(cpfClean.substring(0, 9), pesos1);
        int digito2 = calcularDigito(cpfClean.substring(0, 9) + digito1, pesos2);

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
