#!/bin/sh

set -e

create_files () {
    echo "[+] Creating directory for input data files"
    docker exec namenode mkdir -p /root/input/

    echo "[+] Creating directory for support data"
    docker exec namenode mkdir -p /root/support-data/

    echo "[+] Creating directory for jars"
    docker exec namenode mkdir -p /root/jars

    echo "[+] Copying input data file to container"
    docker cp generated-data/input.txt namenode:/root/input/

    echo "[+] Copying input file to HDFS"
    docker exec namenode hdfs dfs -put /root/input/ input

    echo "[+] Copying input map file to container"
    docker cp generated-data/areas.txt namenode:/root/support-data/

    echo "[+] Copying input map file to HDFS"
    docker exec namenode hdfs dfs -put /root/support-data/ support-data

    echo "[+] Compiling jar"
    sbt clean compile assembly

    echo "[+] Copying jar to container"
    docker cp target/scala-2.12/HW1-assembly-0.1.jar namenode:/root/jars/
}

run_map_reduce () {
    echo "[+] Executing jar"
    docker exec namenode hadoop jar /root/jars/HW1-assembly-0.1.jar input output

    echo "[+] Viewing output directory contents"
    docker exec namenode hdfs dfs -ls output/

    echo "[+] Viewing output file contents"
    docker exec namenode hdfs dfs -text output/part-r-00000
}

cleanup () {
    echo "[+] Removing input directory from container"
    docker exec namenode rm -r /root/input/

    echo "[+] Removing support data directory from container"
    docker exec namenode rm -r /root/support-data/

    echo "[+] Removing jar directory from container"
    docker exec namenode rm -r /root/jars/

    echo "[+] Removing input directory from HDFS"
    docker exec namenode hdfs dfs -rm -r input/

    echo "[+] Removing support data directory from container"
    docker exec namenode hdfs dfs -rm -r support-data/

    echo "[+] Removing output directory from HDFS"
    docker exec namenode hdfs dfs -rm -r output/
}

create_files
run_map_reduce
cleanup
