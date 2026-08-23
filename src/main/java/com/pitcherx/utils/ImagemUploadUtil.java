package com.pitcherx.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class ImagemUploadUtil {

    public ImagemUploadUtil() {
    }

    private static final List<String> EXTENSOES_PERMITIDAS = List.of(".jpg", ".jpeg", ".png", ".webp");
    private static final long TAMANHO_MAXIMO_BYTES = 10 * 1024 * 1024;

    @Value("${app.upload.dir}")
    private String diretorioUpload;

    @Value("${app.upload.base-url}")
    private String baseUrl;

    public String salvarImagem(MultipartFile arquivo) {
        validarArquivo(arquivo);

        try {
            Path diretorio = java.nio.file.Paths.get(diretorioUpload).toAbsolutePath().normalize();
            Files.createDirectories(diretorio);

            String extensao = obterExtensao(arquivo.getOriginalFilename());
            String nomeArquivo = java.util.UUID.randomUUID() + extensao;

            Path destino = diretorio.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/" + nomeArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem: " + e.getMessage(), e);
        }
    }

    public void deletarImagem(String urlImagem) {
        if (urlImagem == null || urlImagem.isBlank()) {
            return;
        }
        try {
            String nomeArquivo = urlImagem.substring(urlImagem.lastIndexOf('/') + 1);
            Path caminho = Paths.get(diretorioUpload).toAbsolutePath().normalize().resolve(nomeArquivo);
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar a imagem: " + e.getMessage(), e);
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo enviado.");
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw new IllegalArgumentException("O arquivo excede o tamanho máximo permitido (5MB).");
        }

        String extensao = obterExtensao(arquivo.getOriginalFilename());
        if (!EXTENSOES_PERMITIDAS.contains(extensao)) {
            throw new IllegalArgumentException("Formato de imagem não suportado. Use: " + EXTENSOES_PERMITIDAS);
        }
    }

    private String obterExtensao(String nomeOriginal) {
        if (nomeOriginal == null || !nomeOriginal.contains(".")) {
            throw new IllegalArgumentException("Arquivo sem extensão válida.");
        }
        return nomeOriginal.substring(nomeOriginal.lastIndexOf('.')).toLowerCase();
    }
}
