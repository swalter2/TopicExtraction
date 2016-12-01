#!/usr/bin/env bash
mvn clean && mvn install
mvn exec:java -Dexec.mainClass="process.Process" -Dexec.args="/Users/swalter/Git/TopicExtraction/TopicExtraction/index_small topics.txt"


