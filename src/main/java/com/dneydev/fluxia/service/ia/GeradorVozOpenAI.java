package com.dneydev.fluxia.service.ia;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementação real usando o modelo de text-to-speech da OpenAI via Spring AI.
 * Só ativa com o perfil "ai" e OPENAI_API_KEY configurada.
 */
@Service
@Profile("ai")
public class GeradorVozOpenAI implements GeradorVoz {

    private final OpenAiAudioSpeechModel speechModel;

    public GeradorVozOpenAI(OpenAiAudioSpeechModel speechModel) {
        this.speechModel = speechModel;
    }

    @Override
    public byte[] gerarAudio(String texto) {
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .build();

        SpeechResponse response = speechModel.call(new SpeechPrompt(texto, options));
        return response.getResult().getOutput();
    }
}