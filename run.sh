#!/bin/sh

set -e

cleanup_previous_job_data () {
    echo "--- PREVIOUS JOB DATA CLEANUP STAGE ---"
    echo "[Container] Checking input directory existence"
    if docker exec namenode test -d /root/input/; then
        echo "[Container] Removing input directory"
        docker exec namenode rm -r /root/input/
        echo
    fi

    echo "[Container] Checking support data directory existence"
    if docker exec namenode test -d /root/support-data/; then
        echo "[Container] Removing support data directory"
        docker exec namenode rm -r /root/support-data/
        echo
    fi

    echo "[Container] Checking jar directory existence"
    if docker exec namenode test -d /root/jars/; then
        echo "[Container] Removing jar directory"
        docker exec namenode rm -r /root/jars/
        echo
    fi

    echo "[HDFS] Checking input directory existence"
    if docker exec namenode hdfs dfs -test -d input/; then
        echo "[HDFS] Removing input directory"
        docker exec namenode hdfs dfs -rm -r input/
        echo
    fi

    echo "[HDFS] Checking support data directory existence"
    if docker exec namenode hdfs dfs -test -d support-data/; then
        echo "[HDFS] Removing support data directory"
        docker exec namenode hdfs dfs -rm -r support-data/
        echo
    fi

    echo "[HDFS] Checking output directory existence"
    if docker exec namenode hdfs dfs -test -d output/; then
        echo "[HDFS] Removing output directory"
        docker exec namenode hdfs dfs -rm -r output/
        echo
    fi

    echo
}

create_files () {
    echo "--- CREATING INPUT FILES STAGE ---"
    echo "[Container] Creating directory for input data files"
    docker exec namenode mkdir -p /root/input/

    echo "[Container] Creating directory for support data"
    docker exec namenode mkdir -p /root/support-data/

    echo "[Container] Creating directory for jars"
    docker exec namenode mkdir -p /root/jars

    echo

    echo "[Container] Copying input data file"
    docker cp generated-data/input.txt namenode:/root/input/

    echo "[HDFS] Copying input file"
    docker exec namenode hadoop fs -mkdir -p . # Create directory for root user
    docker exec namenode hdfs dfs -put /root/input/ input

    echo "[Container] Copying input areas file"
    docker cp generated-data/areas.txt namenode:/root/support-data/

    echo "[HDFS] Copying input areas file to HDFS"
    docker exec namenode hdfs dfs -put /root/support-data/ support-data

    echo "[Host] Compiling jar"
    sbt clean compile assembly

    echo "[Container] Copying jar"
    docker cp target/scala-2.12/HW1-assembly-0.1.jar namenode:/root/jars/

    echo
}

run_map_reduce () {
    echo "--- MAP REDUCE RUNNING STAGE ---"
    echo "[Container] Executing jar"
    docker exec namenode hadoop jar /root/jars/HW1-assembly-0.1.jar input output
    echo

    echo "[HDFS] Viewing output directory contents"
    docker exec namenode hdfs dfs -ls output/
    echo

    echo "[HDFS] Viewing output file contents"
    docker exec namenode hdfs dfs -text output/part-r-00000
    echo
}

cleanup_previous_job_data
create_files
run_map_reduce
