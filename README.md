# NUMA_mapreduce



ENVIAR FICHEROS A HDFS
levantar el docker modo pseudodistribuido:
'''
docker run -it sequenceiq/hadoop-docker:2.7.1 /etc/bootstrap.sh -bash
'''

en el bash de hadoop, ver que procesos tenemos corriendo;
'''
jps
'''

y ver la ruta de los binarios:
'''
cd $HADOOP_PREFIX
'''


HADOOP UI


desde un terminal, obtener ip del contenedor:
'''
docker inspect 15f915157fe5 | grep IPAddress
'''

con la ip obtenida accedemos a hadoop ui:

'''
http://nuestraip:8088/cluster/nodes
'''
