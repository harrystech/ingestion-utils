build-dependencies: build-postgres-utils build-scala-jooq-tables build-orika-utils

build-postgres-utils:
	make build-package package="scala-postgres-utils"

build-scala-jooq-tables:
	mkdir ../scala-jooq-tables/lib || true
	cp lib/scala-postgres* ../scala-jooq-tables/lib/
	make build-package package="scala-jooq-tables"

build-orika-utils:
	make build-package package="orika-utils"

build-package:
	docker build -t $(package) -f ../$(package)/Dockerfile ../$(package)/.
	docker rm $(package)_build || true
	docker create --name $(package)_build $(package)
	docker cp $(package)_build:/app/target/ ./tmp
	docker rm $(package)_build
	mv tmp/*/*/*.jar lib/

build-hyppo-manager:
	mkdir ../hyppo-manager/lib || true
	cp lib/*.jar ../hyppo-manager/lib/
	make copy-libs project="hyppo-manager"
	docker build -t hyppo-manager -f ../hyppo-manager/Dockerfile ../hyppo-manager/.

dockerize-project:
	make copy-libs project="$(project)"
	cp docker/*.template ../$(project)/
	ln -s docker/docker-compose.yaml ../$(project)/docker-compose.yaml

copy-libs:
	mkdir ../$(project)/lib || true
	cp lib/*.jar ../$(project)/lib/
