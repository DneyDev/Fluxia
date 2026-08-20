package com.dneydev.fluxia.service.ia;

import org.springframework.web.multipart.MultipartFile;

public interface TranscritorAudio {
    String transcrever(MultipartFile arquivoAudio);
}