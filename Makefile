build-dependencies: build-postgres-utils build-scala-jooq-tables build-orika-utils

build-postgres-utils:
	make build-package package="scala-postgres-utils"
	mv tmp/*/*.jar lib/

build-scala-jooq-tables:
	mkdir ../scala-jooq-tables/lib || true
	cp lib/scala-postgres* ../scala-jooq-tables/lib/
	make build-package package="scala-jooq-tables"
	mv tmp/*/*/*.jar lib/

build-orika-utils:
	make build-package package="orika-utils"
	mv tmp/*/*/*.jar lib/

build-package:
	docker build -t $(package) -f ../$(package)/Dockerfile ../$(package)/.
	docker rm $(package)_build || true
	docker create --name $(package)_build $(package)
	docker cp $(package)_build:/app/target/ ./tmp
	docker rm $(package)_build
	mkdir lib || true
	# mv tmp/*/*/*.jar lib/

build-hyppo-manager:
	make build-dependencies || true
	make copy-libs project="hyppo-manager"
	. ~/.artifactory/config
	bash generate_artifactory_creds.sh
	docker build -t hyppo-manager -f ../hyppo-manager/Dockerfile ../hyppo-manager/.

dockerize-project:
	make copy-libs project="$(project)"
	cp docker/*.template ../$(project)/
	ln -s `pwd`/docker/docker-compose.yaml ../$(project)/docker-compose.yaml

copy-libs:
	mkdir ../$(project)/lib || true
	cp lib/*.jar ../$(project)/lib/
