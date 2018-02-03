# NUMA_mapreduce


INICIAR DOCKER CON HADOOP
Nos traemos imagen y lo ejecutamos
```
docker pull sequenceiq/hadoop-docker:2.7.1
docker run -it sequenceiq/hadoop-docker:2.7.1 /etc/bootstrap.sh -bash
```

En el bash de hadoop, ver que procesos tenemos corriendo:
```
jps
```

Movernos a ruta de los binarios:
```
cd $HADOOP_PREFIX
```

COMPILAR Y ENVIAR JAR Y EJEMPLO

Compilar con maven:
```
mvn clean
mvn package
```

Obtener id de docker y enviar ejemplo y jar
```
docker ps
docker cp ejemplo1.txt containerid:/tmp/ejemplo1.txt
docker cp /target/NUMAMapReduce1-1.0-SNAPSHOT.jar containerid:/tmp/ejemplo.txt
```


ACCEDER A HADOOP UI

desde un terminal, obtener ip del contenedor:
```
docker inspect 15f915157fe5 | grep IPAddress
```

con la ip obtenida accedemos a hadoop ui:

```
http://nuestraip:8088/cluster/nodes
```
