# Desaﬁo backend pleno
## Execução
### Requisitos
- Java 11
- Maven
- Docker
- Docker Compose

### Windows
Executar o arquivo _.bat_ na raiz do projeto.
```cmd
./execute.bat
```
### Unix-like
Executar o arquivo _.sh_ na raiz do projeto.
```bash
sh ./execute.sh
```
## Log
### Windows
Para visualizar os _logs_ de execução no windows utilizar o comando.
```bash
docke-compose logs -f
```
### Unix-like
Os _logs_ de execução são salvos no arquivo log.txt na raiza do projeto.
## Parar execução
```bash
docker-compose down
```
