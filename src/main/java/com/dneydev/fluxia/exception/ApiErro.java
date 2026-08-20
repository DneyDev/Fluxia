package com.dneydev.fluxia.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErro(
        LocalDateTime timestamp,
        int status,
        String erro,
        List<String> mensagens
) {}