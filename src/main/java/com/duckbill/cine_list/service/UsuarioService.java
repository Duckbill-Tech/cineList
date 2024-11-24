package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.LoginResponseDTO;
import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.infra.security.TokenService;
import com.duckbill.cine_list.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    //private final EmailService emailService; TODO

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        //this.emailService = emailService; TODO
    }

    // Metodo para registrar um novo usuário e retornar o token
    public ResponseDTO register(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já está em uso.");
        }

        if (!isValidCPF(usuarioDTO.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario.setId(UUID.randomUUID());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario savedUsuario = usuarioRepository.save(usuario);

        String token = tokenService.generateToken(savedUsuario);

        return new ResponseDTO(savedUsuario.getNome(), token);
    }

    // Metodo para Login do usuário
    public LoginResponseDTO login(String email, String senha) {
        // Procurando o usuário no banco de dados
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        // Verificando a senha
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        // Gerando o token
        String token = tokenService.generateToken(usuario);

        // Retornando o ResponseDTO com nome do usuário e o token gerado
        return new LoginResponseDTO(usuario.getNome(), usuario.getEmail(), token);
    }

    // Metodo para pega criar usuario (sem token)
    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        usuario.setId(UUID.randomUUID());

        if (!isValidCPF(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toDto(savedUsuario);
    }

    // Metodo para pega usuario por id
    public Optional<UsuarioDTO> getById(UUID id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toDto);
    }

    // Metodo para pegar todos
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    // Metodo para update usuário
    public ResponseDTO update(UUID id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioDTO.getNome());
                    usuario.setEmail(usuarioDTO.getEmail());

                    if (!usuario.getCpf().equals(usuarioDTO.getCpf())) {
                        usuario.setCpf(usuarioDTO.getCpf());
                    }

                    if (!isValidCPF(usuario.getCpf())) {
                        throw new IllegalArgumentException("CPF inválido");
                    }

                    usuario.setUpdatedAt(LocalDateTime.now());
                    Usuario updatedUsuario = usuarioRepository.save(usuario);
                    String newToken = tokenService.generateToken(updatedUsuario);

                    return new ResponseDTO(updatedUsuario.getNome(), newToken);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Metodo para deletar usuário
    public void delete(UUID id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setDeletedAt(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }

    // Metodo para buscar o usuário pelo e-mail
    public Optional<UsuarioDTO> getByEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.map(UsuarioMapper::toDto);
    }

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

    // TODO
//    public String generateAndSendPasswordResetToken(String email) {
//        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
//
//        if (optionalUsuario.isEmpty()) {
//            return null; // Email não encontrado
//        }
//
//        Usuario usuario = optionalUsuario.get();
//        String token = UUID.randomUUID().toString(); // Gera token automaticamente com UUID
//        usuario.setPasswordResetToken(token);
//        usuario.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
//
//        usuarioRepository.save(usuario);
//        emailService.sendPasswordResetEmail(email, token);
//        return token;
//    }
//
//    public boolean resetPasswordWithToken(String token, String newPassword) {
//        Optional<Usuario> optionalUsuario = usuarioRepository.findByPasswordResetToken(token);
//        if (optionalUsuario.isPresent()) {
//            Usuario usuario = optionalUsuario.get();
//            if (usuario.getTokenExpirationTime() != null && usuario.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
//                return false; // Token expirado
//            }
//            usuario.setSenha(passwordEncoder.encode(newPassword));
//            usuario.setPasswordResetToken(null);
//            usuario.setTokenExpirationTime(null);
//            usuarioRepository.save(usuario);
//            return true;
//        }
//        return false;
//    }

}