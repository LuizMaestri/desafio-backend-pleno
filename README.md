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
Executar o arquivo _.sh_ na raiz do projeto (será solicitado a senha do usuário da máquina).
```bash
sh ./execute.sh
```
## Log

Para visualizar os _logs_ de execução utilizar o comando.
```bash
docke-compose logs -f
```

## Parar execução
```bash
docker-compose down
```
