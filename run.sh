echo "Копирование JAR-файл в name ноду..."
docker cp ./build/libs/lab4-vllid-parallel-1.0-SNAPSHOT.jar namenode:/tmp/
echo "Копирование JAR-файл в name ноду завершено."
echo "Копирование csv-файлов в name ноду..."
docker cp ./0.csv namenode:/tmp/
docker cp ./1.csv namenode:/tmp/
docker cp ./2.csv namenode:/tmp/
docker cp ./3.csv namenode:/tmp/
docker cp ./4.csv namenode:/tmp/
docker cp ./5.csv namenode:/tmp/
docker cp ./6.csv namenode:/tmp/
docker cp ./7.csv namenode:/tmp/
echo "Копирование csv-файлов в name ноду завершено."

docker exec -it namenode /bin/bash
cd tmp/

echo "Создание папки для csv-файлов в hdfs..."
hdfs dfs -mkdir -p /user/root
hdfs dfs -mkdir /user/root/input
echo "Создание папки для csv-файлов в hdfs завершено."
echo "Копирование csv-файлов из хостовой папки контейнера в hdfs..."
echo "Копирование 0.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 0.csv /user/root/input
echo "Копирование 0.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 1.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 1.csv /user/root/input
echo "Копирование 1.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 2.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 2.csv /user/root/input
echo "Копирование 2.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 3.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 3.csv /user/root/input
echo "Копирование 3.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 4.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 4.csv /user/root/input
echo "Копирование 4.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 5.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 5.csv /user/root/input
echo "Копирование 5.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 6.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 6.csv /user/root/input
echo "Копирование 6.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование 7.csv из хостовой папки контейнера в hdfs..."
hdfs dfs -put 7.csv /user/root/input
echo "Копирование 7.csv из хостовой папки контейнера в hdfs завершено."

echo "Копирование csv-файлов из хостовой папки контейнера в hdfs завершено."

echo "Запуск задания MapReduce с параметрами для hadoop..."
hadoop jar /tmp/lab4-vllid-parallel-1.0-SNAPSHOT.jar input output 1 128

echo "Директория с результатами:"
hadoop fs -ls /user/root/output

echo "Копирование результата из HDFS в файловую систему контейнера..."
hadoop fs -get /user/root/output/part-r-00000 ./part-r-00000
echo "Копирование результата из HDFS в файловую систему контейнера завершено."

echo "Очистка предыдущих результатов выполнений задачи..."
hadoop fs -rm -r /user/root/output
hadoop fs -rm -r /user/root/output-mid

echo "Копирование результата из HDFS на локальную машину..."
exit
docker cp namenode:/tmp/part-r-00000 ./part-r-00000
echo "Копирование результата из HDFS на локальную машину завершено."