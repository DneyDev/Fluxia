package com.dneydev.fluxia.service.ia;

import com.dneydev.fluxia.dto.ComandoInterpretado;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementação real usando Spring AI + OpenAI. Só é ativada com o perfil "ai"
 * (spring.profiles.active=ai) e uma OPENAI_API_KEY válida.
 *
 * Diferença importante em relação ao AssistenteIAStub: aqui o próprio modelo
 * decide chamar registrarTransacao() ou consultarSaldoMesAtual() (Tool Calling),
 * então a resposta que chega já é o resultado final da ação — não precisamos
 * mais montar um ComandoInterpretado manualmente e passar pelo switch do
 * AssistenteService. Isso é uma evolução pendente: quando plugar essa classe
 * de verdade, vale simplificar o AssistenteService para não duplicar a decisão.
 */
@Service
@Profile("ai")
public class AssistenteIAOpenAI implements AssistenteIA {

    private final ChatClient chatClient;

    public AssistenteIAOpenAI(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public ComandoInterpretado interpretarComando(String textoComando) {
        String resposta = chatClient.prompt()
                .system("""
                        Você é um assistente financeiro. Interprete o comando do usuário
                        em português e use as ferramentas disponíveis para registrar
                        transações ou consultar o saldo. Responda de forma direta.
                        """)
                .user(textoComando)
                .call()
                .content();

        return new ComandoInterpretado("EXECUTADO_VIA_TOOL", resposta, null, null, null, null);
    }
}