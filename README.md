# Fluxia 

API inteligente de orçamento que processa **comandos de voz e texto** relacionados a transações financeiras, construída com **Spring Boot** e **Spring AI**.

Projeto desenvolvido como desafio prático de Spring AI (trilha Santander & DIO), evoluído do zero em etapas públicas de commit, com foco em entender como conectar IA a uma aplicação real respeitando responsabilidades e organização de código.

---

## O que o projeto faz

O fluxo principal é:

1. Recebe um comando (texto ou arquivo de áudio) do usuário
2. Transcreve o áudio em texto
3. Usa IA para entender a intenção do comando (registrar transação ou consultar saldo)
4. Executa a ação real na aplicação (grava no banco / calcula saldo)
5. Gera uma resposta final, inclusive em formato de voz

Exemplo: o usuário envia um áudio dizendo *"gastei 50 reais em mercado"* → a API transcreve, entende que é uma despesa, categoriza como "mercado", registra a transação no banco e devolve uma confirmação (em texto e voz).

---

## Arquitetura

A decisão de design mais importante do projeto foi desacoplar **"o que a aplicação precisa da IA"** de **"qual implementação está respondendo"**, usando interfaces:

| Interface | Stub (padrão, sem custo) | Implementação real (perfil `ai`) |
|---|---|---|
| `AssistenteIA` | Regex simples | Spring AI `ChatClient` + Tool Calling |
| `TranscritorAudio` | Lê o "áudio" como texto puro | OpenAI Whisper |
| `GeradorVoz` | Retorna texto como bytes | OpenAI TTS |

Isso permite desenvolver e testar o fluxo completo **sem gastar com API paga**, e ativar a IA de verdade só configurando uma variável de ambiente e um profile — sem tocar no resto do código.


O Tool Calling real (`FinancasTools`) reaproveita o mesmo `TransacaoService` usado pelos endpoints REST — a IA não tem um caminho paralelo de acesso a dados, ela "aperta os mesmos botões" que a API já expõe.

---

## Tecnologias usadas

- Java 17
- Spring Boot 3
- Spring AI (`ChatClient`, Tool Calling, OpenAI starter)
- Spring Data JPA + H2 (banco em memória)
- Bean Validation
- Lombok
- JUnit 5 + Mockito + AssertJ

---

## Como executar

```bash
git clone https://github.com/DneyDev/fluxia.git
cd fluxia
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`, já com o banco H2 em memória configurado (console em `/h2-console`).

### Ativando a IA real (opcional)

```bash
export OPENAI_API_KEY=sua-chave-aqui
./mvnw spring-boot:run --spring.profiles.active=ai
```

---

## Como testar o fluxo principal

**1. Comando de texto:**
```bash
curl -X POST http://localhost:8080/api/assistente/comando \
  -H "Content-Type: application/json" \
  -d '{"comando":"gastei 50 reais em mercado"}'
```

**2. Comando de "áudio" (arquivo texto simulando transcrição):**
```bash
echo "gastei 30 reais em uber" > comando.txt
curl -X POST http://localhost:8080/api/assistente/audio -F "arquivo=@comando.txt"
```

**3. Fluxo completo (áudio → resposta em "voz"):**
```bash
curl -X POST http://localhost:8080/api/assistente/audio/resposta-em-voz \
  -F "arquivo=@comando.txt" --output resposta.txt
```

**4. CRUD direto de transações:**
```bash
curl http://localhost:8080/api/transacoes
curl "http://localhost:8080/api/transacoes/saldo?inicio=2026-08-01&fim=2026-08-31"
```

**5. Testes automatizados:**
```bash
./mvnw test
```

---

## Melhoria implementada em relação ao projeto base

Além do fluxo pedido no desafio, a principal evolução foi arquitetural: em vez de depender direto da API da OpenAI, criei uma camada de abstração (`AssistenteIA`, `TranscritorAudio`, `GeradorVoz`) com implementações stub e real convivendo por Spring Profile. Isso resolve um problema prático real — desenvolver e testar sem gastar com API — e é o tipo de padrão (Strategy) que se usa em projetos profissionais para isolar dependências externas caras ou instáveis.

---

## O que aprendi com o desafio

- Como o Spring AI conecta `ChatClient`, prompts e Tool Calling numa aplicação Spring Boot real
- Que Tool Calling não é "mágica": é a IA decidindo qual método Java chamar, com os parâmetros que ela mesma extrai do texto
- A importância de separar a lógica de negócio (`TransacaoService`) de quem está "pilotando" ela (IA ou usuário via REST direto)
- Como usar `@Profile` para alternar implementações inteiras sem `if/else` espalhado pelo código
- Que dá pra desenvolver e testar um fluxo inteiro de IA sem gastar dinheiro, usando stubs bem pensados no lugar de mocks genéricos

---

## Status do desenvolvimento
> Projeto Concluído.

- [x] Setup inicial do projeto
- [x] Modelagem do domínio
- [x] Persistência
- [x] Endpoints REST (CRUD)
- [x] Integração com IA (Spring AI)
- [x] Transcrição de áudio
- [x] Tool Calling
- [x] Geração de voz
- [x] Testes
- [x] Documentação final
