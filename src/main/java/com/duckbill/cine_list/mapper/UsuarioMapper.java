package com.duckbill.cine_list.mapper;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.dto.UsuarioDTO;

public class UsuarioMapper {

    public static UsuarioDTO toDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt(),
                usuario.getDeletedAt()
        );
    }

    public static Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioDTO.getId());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setCreatedAt(usuarioDTO.getCreatedAt());
        usuario.setUpdatedAt(usuarioDTO.getUpdatedAt());
        usuario.setDeletedAt(usuarioDTO.getDeletedAt());
        return usuario;
    }
}