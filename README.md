# Price Quote
Cotação de culturas agrícolas negociadas na BM&F (Bolsa de Mercadorias e Futuros).

## Requisitos
- Maven 3.6.3
- Java 14 --enable-preview
- Docker
- Docker Compose

## Variaveis de Ambiente da aplicação

| Nome | Descrição | Valor Padrão | Obrigatório |
| -- | -- | -- | -- |
| ENV | Qual Enviroment a Aplicação está | staging |Não |
| JWT_SECRET_KEY | Secret usado na validação do token JWT | stubJwt | Não |
| DATASOURCE_URL | Host de conexão postgresql || Sim |
| DATABASE_USERNAME | Usuário de conexão postgresql || Sim |
| DATABASE_PASSWORD | Senha de conexão postgresql || Sim |
| DATABASE_CONNECTION_MIN | mínimo de conexões ociosas do pool de conexão |10| Não |
| DATABASE_CONNECTION_MAX | maximo de conexões configuradas |20| Não |
| HTTP_QUANDL_HOST | Host da API QUANDL || Sim |
| HTTP_QUANDL_KEY | Key de acesso a API QUANDL || Sim |
| HTTP_QUANDL_RETRY_MAX_ATTEMPTS | Maximo de retentativas de acesso a API QUANDL  |3| Não |
| HTTP_QUANDL_RETRY_BACKOFF_DELAY | Intervalo das retentativas em milisegundos  |2000| Não |
| HTTP_AWESOME_HOST | Host da API AWESOME || Sim |
| HTTP_AWESOME_RETRY_MAX_ATTEMPTS | Maximo de retentativas de acesso a API Awesome |3| Não |
| HTTP_AWESOME_RETRY_BACKOFF_DELAY | Intervalo das retentativas em milisegundos |2000| Não |

## Buildando e executando os testes 
### Inicie container do banco de dados seguido do comando maven

```sh
docker-compose up db -d
```

```sh
mvn clean install
```

## Executando a aplicação dentro do container Docker
Verifique o arquivo `Dockerfile` se a variavel `HTTP_QUANDL_KEY` corresponde ao token correto para utilização do serviço da QUANDL 
### Realize o build da aplicação (sem testes)

```sh
mvn clean install -DskipTests
```

### Pare os containers dockers da aplicação, faça o build, e inicie novamente
```sh
docker-compose down
```

```sh
docker-compose build
```

```sh
docker-compose up
```

A aplicação fica disponivel na porta `8080`

## Chamando o endpoint de cotação
- A API possui autenticação JWT no seu endpoint, e usa a variavel ambiente `JWT_SECRET_KEY` para fazer a validação, caso a mesma esteja setada como `stubJwt` o token a ser utilizado na chamada é `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.29KNIUWCjzIfetCqXVcFL3Ok7OJxdQAY9qCYNGaS_KQ` caso a variavel tenha sido alterado basta acessar https://jwt.io/ gerar um hash (payload deve ser {}) usando o novo secret e setar no header como `Bearer {token}`.

- Com a aplicação ja iniciada basta preencher a chamada abaixo preenchendo os parametros seguindos as respectivas regras
  - {culture} - deve seguir os dados `CEPEA` disponivel em https://www.quandl.com/data/CEPEA-Center-for-Applied-Studies-on-Applied-Economics-Brazil
  - {date} - data no formato YYYY-MM-DD
  - {token} - pode ser o token padrão informado acima, ou o novo gerado baseado no secret

### CURL da chamada
```
curl --location --request GET 'localhost:8080/v1/quote/{culture}?date={date}' \
--header 'Authorization: {token}'
```

### Formato retorno sucesso - status 200
```json
{
    "server": "3ae9382055ca",
    "version": "0.0.1",
    "data": [
        {
            "culture": "CALF_C",
            "value": 2493.243,
            "date": "2020-12-03"
        }
    ]
}
```

### Formato retorno erro - status 500 - problema na chamada da QUANDL
```json
{
    "message": "Error in call Quandl"
}
```



