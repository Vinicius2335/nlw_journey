# Dependencias Usadas
- [Goapi-gen](https://github.com/discord-gophers/goapi-gen)
	- é o lombock do go
	-  ``goapi-gen --out ./internal/api/spec/journey.gen.spec.go ./internal/api/spec/journey.spec.json``
	-  criou 1020 linhas
	-  ctrl + f ServerInterface
   	- ela diz todos os métodos que devem ser implementados para a api funcionar
- [Sqlc](https://github.com/sqlc-dev/sqlc-gen-go)
  - [Sqlc + Mysql](https://docs.sqlc.dev/en/stable/tutorials/getting-started-mysql.html) 
  - [Sqlc + Postgres](https://docs.sqlc.dev/en/stable/guides/using-go-and-pgx.html) 
  - Conexao com o banco de dados
- [Tern](https://github.com/jackc/tern) - funciona somente com postgres
  - Migrations
  - ``tern init ./internal/pgstore/migrations``
  - ``tern new --migrations ./internal/pgstore/migrations create_trips_table``
  - ``tern migrate --migrations ./internal/pgstore/migrations --config ./internal/pgstore/migrations/tern.conf``

# Não esquecer
- editor swagger
- go mod init -> iniciar um projeto go
- go mod tidy -> instalou automaticamente todas as dependencias pendentes apos usar o goapi-gen
- go get -u ./... -> atualiza as dependencias para a mais recentes, já que 'go mod tidy' instala as versoes já instaladas na sua maquina
- ORMs não são considerados boa pratica em go, usa-se 'sqlc'
- Para queries dinamicas usa-se geradores de pacotes como 'goqu' ou 'go squirrel(mais utilizado)'