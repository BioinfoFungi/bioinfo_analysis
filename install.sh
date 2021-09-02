#!/usr/bin/bash
git pull
./mvnw clean
 ./mvnw install -Dmaven.test.skip=true
dir=`pwd`
app="bioinfo-0.0.1-SNAPSHOT.jar"
jar="${dir}/target/${app}"

if [ ! -f $jar ];then
  echo "build failure！！！"
  exit 8
fi

pid=$(jps | grep "bioinfo-0.0.1-SNAPSHOT.jar" | awk '{print $1}')

if [ $pid ]
then
  echo "kill ${pid}"
  kill ${pid}
fi

echo $jar
nohup java -jar $jar 2>&1 > bioinfo.log &