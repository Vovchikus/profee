## How to run

**Native way**:

REQUIRED: docker (https://docs.docker.com/install/)

(**Very important**: natively maven still will build docker image, so you should have Docker installed and have ability to run docker without SUDO (sudo gpasswd -a $USER docker))

https://docs.docker.com/install/linux/linux-postinstall/

If you don't want docker-image to be build, than just remove dockerfile-maven-plugin from pom.xml


```markdown 
mvn clean install  
mvn spring-boot:run
```

**Docker way**:

REQUIRED: docker-compose (https://docs.docker.com/compose/install/), tested under 2.1 docker-compose version

```markdown
docker-compose up -d
```

in the project root

======================

## Documentation

* https://github.com/Vovchikus/profee/wiki/Profee-project