# NUMA_mapreduce


## INICIAR DOCKER CON HADOOP

Nos traemos imagen y lo ejecutamos
```
docker pull sequenceiq/hadoop-docker:2.7.1
docker run -it sequenceiq/hadoop-docker:2.7.1 /etc/bootstrap.sh -bash
```

En el bash del docker con hadoop, ver que procesos tenemos corriendo:
```
jps
```

## COMPILAR Y ENVIAR JAR Y EJEMPLO

Desde nuestro terminal compilar con maven:
```
mvn clean
mvn package
```

Después obtener id de docker y enviar ejemplo y jar al contenedor
```
docker ps
docker cp ejemplo1.txt containerid:/tmp
docker cp /target/NUMAMapReduce1-1.0-SNAPSHOT.jar containerid:/usr/local/hadoop/share/hadoop/mapreduce
```

## COPIAR A HDFS NUESTRO INPUT Y EJECUTAR NUESTRO TRABAJO

En el bash del contenedor de hadoop:
```
cd $HADOOP_PREFIX
bin/hdfs dfs -put /tmp/ejemplo1.txt input
bin/hadoop jar share/hadoop/mapreduce/NUMAMapReduce1-1.0-SNAPSHOT.jar hadoopWordCount input/ejemplo1.txt output/ejemplo1
```

Revisamos salida:
```
bin/hdfs dfs -cat output/ejemplo1/*
```

## ACCEDER A HADOOP UI

Desde nuestro terminal, obtener ip del contenedor docker:
```
docker inspect containerid | grep IPAddress
```

con la ip obtenida accedemos a hadoop ui:

```
http://nuestraip:8088/cluster/nodes
```
