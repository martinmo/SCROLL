#!/bin/sh
JVM_ARGS="-Duser.language=en -Duser.country=US"

if ! java -version 2>&1 | grep -qE '^(openjdk|java) version "1\.8\..*"$'; then
    # for all JDKs != 1.8
    JVM_ARGS="$JVM_ARGS --add-opens=java.base/java.io=ALL-UNNAMED"
fi

exec java $JVM_ARGS -jar benchmark/target/scala-2.12/benchmarks.jar "$@"
