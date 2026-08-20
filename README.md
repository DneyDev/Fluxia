# Fluxia

API inteligente de orçamento que processa comandos de voz relacionados a transações financeiras, usando Spring Boot + Spring AI.

> 🚧 Projeto em construção — desenvolvido em etapas públicas no GitHub.

## Status
- [x] Setup inicial do projeto
- [x] Modelagem do domínio
- [x] Persistência
- [x] Endpoints REST (CRUD)
- [x] Integração com IA (Spring AI) — base + stub
- [x] Transcrição de áudio
- [x] Tool Calling — implementado e documentado (stub ativo + implementação real como referência)
- [x] Geração de voz — base + stub + referência real
- [ ] Testes
- [ ] Documentação final

## Sobre a integração com IA

O projeto usa a interface `AssistenteIA` para desacoplar a lógica de negócio
de qual "cérebro" está interpretando os comandos:

- **`AssistenteIAStub`** (perfil padrão): usa expressões regulares simples,
  não depende de API paga, ideal para desenvolver e testar o fluxo completo.
- **`AssistenteIAOpenAI`** (perfil `ai`): usa Spring AI `ChatClient` com
  Tool Calling real (`FinancasTools`), onde o modelo decide sozinho quando
  registrar uma transação ou consultar o saldo.

Para ativar a versão real:
1. Configure a variável de ambiente `OPENAI_API_KEY`
2. Ative o perfil: `spring.profiles.active=ai`

Ambas implementações resolvem o mesmo contrato (`ComandoInterpretado`), então
o resto da aplicação não muda dependendo de qual está ativa.