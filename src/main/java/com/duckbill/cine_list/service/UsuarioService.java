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

    public Usuario create(Usuario usuario) {
        usuario.setCreatedAt(LocalDateTime.now()); // Define a data de criaçao
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
}