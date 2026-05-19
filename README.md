# Pior Filme - REST API Golden Raspberry Awards

Uma aplicação REST API que fornece informações sobre os filmes indicados e vencedores da categoria **Pior Filme** do **Golden Raspberry Awards** (Framboesa de Ouro), com análise detalhada dos intervalos entre prêmios de produtores consecutivos.

## 🎯 Características

- ✅ **REST API com nível 2 de Richardson**: Múltiplos recursos com métodos HTTP apropriados
- ✅ **Banco de dados embarcado**: Utiliza H2 em memória, sem necessidade de instalação externa
- ✅ **Carregamento automático de dados**: Lê e processa o arquivo CSV ao iniciar a aplicação
- ✅ **Análise de prêmios**: Calcula produtores com maior e menor intervalo entre prêmios consecutivos
- ✅ **Testes de integração**: Valida todos os endpoints e a integridade dos dados
- ✅ **Sem dependências externas**: Tudo em memória, pronto para usar

## 🛠️ Tecnologias Utilizadas

- **Java 21**: Linguagem principal
- **Spring Boot 4.0.6**: Framework Web
- **Spring Data JPA**: Persistência de dados
- **H2 Database**: Banco de dados embarcado em memória
- **OpenCSV**: Leitura do arquivo CSV
- **JUnit 5**: Framework de testes
- **Maven**: Gerenciador de dependências

## 📁 Estrutura do Projeto

```
PiorFilme/
├── src/
│   ├── main/
│   │   ├── java/com/outsera/pior_filme/
│   │   │   ├── PiorFilmeApplication.java          # 🚀 Classe principal
│   │   │   ├── controller/
│   │   │   │   └── MovieController.java           # REST API endpoints
│   │   │   ├── service/
│   │   │   │   ├── MovieService.java              # Lógica de filmes
│   │   │   │   └── AwardIntervalService.java      # Cálculo de intervalos
│   │   │   ├── model/
│   │   │   │   ├── Movie.java                     # Entidade Movie
│   │   │   │   └── Producer.java                  # Entidade Producer
│   │   │   ├── repository/
│   │   │   │   ├── MovieRepository.java           # Operações DB filmes
│   │   │   │   └── ProducerRepository.java        # Operações DB produtores
│   │   │   ├── loader/
│   │   │   │   └── MovieDataLoader.java           # Carrega dados do CSV
│   │   │   └── dto/
│   │   │       ├── MovieDto.java                  # DTO - Filme
│   │   │       ├── AwardIntervalDto.java          # DTO - Intervalo
│   │   │       └── AwardIntervalResponseDto.java  # DTO - Resposta intervalos
│   │   └── resources/
│   │       ├── application.yaml                   # Configurações
│   │       └── dados/Movielist.csv                # Dados (formato CSV)
│   └── test/
│       └── java/com/outsera/pior_filme/
│           └── controller/
│               ├── MovieControllerIntegrationTest.java
│               └── ProducerAwardIntervalIntegrationTest.java
├── pom.xml                                        # Dependências Maven
├── mvnw e mvnw.cmd                                # Maven Wrapper
└── README.md                                      # Este arquivo
```

## 📊 Estrutura do Banco de Dados

### Tabelas Principais

**movies**
- `id` (PK): Identificador único
- `year`: Ano do filme
- `title`: Título do filme
- `studios`: Estúdios produtores
- `winner`: Se foi vencedor (true/false)

**producers**
- `id` (PK): Identificador único
- `name`: Nome do produtor (UNIQUE)

**movie_producers** (Tabela de Junção)
- `movie_id` (FK): Referência a movie
- `producer_id` (FK): Referência a producer

## 🚀 Como Executar a Aplicação

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+ (ou use o Maven Wrapper incluído)

### 1. Compilar o Projeto

**No Linux/Mac:**
```bash
./mvnw clean compile
```

**No Windows:**
```cmd
mvnw.cmd clean compile
```

### 2. Executar a Aplicação

**No Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**No Windows:**
```cmd
mvnw.cmd spring-boot:run
```

A aplicação iniciará em: **http://localhost:8080**

### 3. Verificar se está funcionando

Acesse qualquer endpoint para validar:
```bash
curl http://localhost:8080/api/movies
```

Ou abra no navegador: `http://localhost:8080/api/movies/producers/intervals`

## 📝 REST API Endpoints

### 1️⃣ Obter todos os filmes

```http
GET /api/movies
```

**Resposta de Sucesso (200 OK):**
```json
[
  {
    "id": 1,
    "year": 1980,
    "title": "Can't Stop the Music",
    "studios": "Associated Film Distribution",
    "producers": ["Allan Carr"],
    "winner": true
  },
  {
    "id": 2,
    "year": 1980,
    "title": "Cruising",
    "studios": "Lorimar Productions, United Artists",
    "producers": ["Jerry Weintraub"],
    "winner": false
  }
]
```

### 2️⃣ Obter apenas filmes vencedores

```http
GET /api/movies/winners
```

**Resposta de Sucesso (200 OK):**
```json
[
  {
    "id": 1,
    "year": 1980,
    "title": "Can't Stop the Music",
    "studios": "Associated Film Distribution",
    "producers": ["Allan Carr"],
    "winner": true
  }
]
```

### 3️⃣ Obter análise de intervalos de prêmios

```http
GET /api/movies/producers/intervals
```

**Resposta de Sucesso (200 OK):**
```json
{
  "min": [
    {
      "producer": "Producer A",
      "interval": 1,
      "previousWin": 2008,
      "followingWin": 2009
    },
    {
      "producer": "Producer B",
      "interval": 1,
      "previousWin": 2018,
      "followingWin": 2019
    }
  ],
  "max": [
    {
      "producer": "Producer X",
      "interval": 99,
      "previousWin": 1900,
      "followingWin": 1999
    },
    {
      "producer": "Producer Y",
      "interval": 99,
      "previousWin": 2000,
      "followingWin": 2099
    }
  ]
}
```

**Significado:**
- `min`: Produtor(es) que ganhou(ganharam) dois prêmios mais rápido (menor intervalo em anos)
- `max`: Produtor(es) com o maior intervalo entre dois prêmios consecutivos
- `interval`: Número de anos entre as duas premiações
- `previousWin`: Ano da primeira vitória
- `followingWin`: Ano da segunda vitória

## 🧪 Executar Testes de Integração

### Executar todos os testes

**No Linux/Mac:**
```bash
./mvnw test
```

**No Windows:**
```cmd
mvnw.cmd test
```

### Executar teste específico

**No Linux/Mac:**
```bash
./mvnw test -Dtest=MovieControllerIntegrationTest
```

**No Windows:**
```cmd
mvnw.cmd test -Dtest=MovieControllerIntegrationTest
```

### Executar com saída detalhada

```bash
./mvnw test -X
```

## 📋 Testes de Integração Inclusos

### MovieControllerIntegrationTest
- ✅ `testGetAllMovies`: Valida retorno de todos os filmes
- ✅ `testGetWinners`: Valida filtro de vencedores
- ✅ `testGetProducersWithAwardIntervals`: Valida cálculo de intervalos
- ✅ `testProducerIntervalDataConsistency`: Valida integridade dos dados
- ✅ `testMovieWinnerDataConsistency`: Valida consistência de vencedores

### ProducerAwardIntervalIntegrationTest
- ✅ `testProducersDataFromCSV`: Valida dados carregados do CSV
- ✅ `testProducerYearConsistency`: Valida anos dentro de intervalo válido
- ✅ `testMinMaxIntervalValues`: Valida cálculo correto de min/max

## 🔧 Configuração (application.yaml)

```yaml
spring:
  application:
    name: pior-filme
  datasource:
    url: jdbc:h2:mem:testdb          # Banco em memória
    driverClassName: org.h2.Driver
    username: sa
    password: 
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop           # Cria/recria schema a cada execução
    show-sql: false
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080
```

## 💾 Console H2 (Opcional)

Para acessar o console do banco de dados durante a execução:

```
URL: http://localhost:8080/h2-console
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: (deixar em branco)
```

## 🌐 Conformidade com Richardson Maturity Model

A API implementa o **Nível 2** de maturidade de Richardson:

| Nível | Critério | Status |
|-------|----------|--------|
| 0 | Endpoint único (RPC) | ❌ Não |
| 1 | Múltiplos recursos com URIs | ✅ Sim |
| 2 | Uso apropriado de HTTP verbs | ✅ Sim |
| 3 | HATEOAS (Hypermedia) | ❌ Não (intencional) |

### Recursos da API:
- `/api/movies` - Recurso de filmes
- `/api/movies/winners` - Sub-recurso de vencedores
- `/api/movies/producers/intervals` - Sub-recurso de análise de intervalos

### Métodos HTTP:
- `GET /api/movies` - Recupera todos os filmes
- `GET /api/movies/winners` - Recupera filmes vencedores
- `GET /api/movies/producers/intervals` - Recupera análise de intervalos

## 📥 Carregamento de Dados do CSV

### Fluxo de Carregamento

1. Aplicação inicia
2. Spring executa `MovieDataLoader` (implementa `CommandLineRunner`)
3. Verifica se banco já tem dados (pula se tiver)
4. Lê arquivo `src/main/resources/dados/Movielist.csv`
5. Para cada linha do CSV:
   - Analisa campos separados por `;`
   - Cria entidade `Movie`
   - Cria ou recupera entidades `Producer`
   - Associa produtores ao filme
   - Salva no banco H2 em memória
6. API pronta para requisições

### Formato do CSV

```csv
year;title;studios;producers;winner
1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes
1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub;
```

### Tratamento de Produtores

O parser trata múltiplos formatos de produtores:
- ✅ Nome único: `Allan Carr`
- ✅ Separados por vírgula: `John Davis, Charlie Gordon`
- ✅ Separados por "and": `Yoram Globus and Menahem Golan`
- ✅ Combinação: `Jerry Weintraub, Rob Reiner and Alan Zweibel`

## 🛡️ Banco de Dados

- **Tipo**: H2 Database
- **Modo**: Em memória (`mem:testdb`)
- **Persistência**: Dados são **perdidos** ao encerrar a aplicação
- **Inicialização**: Automática via Hibernate
- **Recreação**: `ddl-auto: create-drop` recria o schema em cada execução
- **Vantagens**: 
  - Sem instalação externa
  - Sem configuração
  - Perfeito para testes
  - Isolamento entre execuções

## 🐛 Troubleshooting

### Erro: "Cannot find symbol"
```bash
./mvnw clean install
```

### Erro: "Connection refused"
Verifique se a aplicação está rodando:
```bash
./mvnw spring-boot:run
```

### Erro: "Arquivo CSV não encontrado"
Certifique-se que o arquivo existe em: `src/main/resources/dados/Movielist.csv`

### Erro ao executar testes
```bash
./mvnw test -X
```

## 📚 Exemplo de Uso com cURL

```bash
# Listar todos os filmes
curl -X GET http://localhost:8080/api/movies | jq

# Listar apenas vencedores
curl -X GET http://localhost:8080/api/movies/winners | jq

# Obter análise de intervalos
curl -X GET http://localhost:8080/api/movies/producers/intervals | jq
```

## 📞 Suporte

Para dúvidas ou problemas, verifique:
1. Os logs da aplicação durante execução
2. O console H2 para validar dados no banco
3. Os testes para exemplos de uso correto

## 📄 Licença

Projeto de código aberto para análise de dados do Golden Raspberry Awards.

---

**Última atualização**: 2024

