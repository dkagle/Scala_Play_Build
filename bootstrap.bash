#!/bin/bash

echo "Enter Scala Play! Directory ."
read EZBUILD_HOME
echo "Enter database user name."
read USER
echo "Enter database user password."
read PASSWORD
echo "Enter database name."
read DBNAME
echo "Enter database host name."
read HOSTNAME
echo "Enter 1 for MySQL 2 for PostgreSQL 3 for Oracle."
read DB


mkdir $EZBUILD_HOME
cd $EZBUILD_HOME
sbt new playframework/play-scala-seed.g8

mkdir $EZBUILD_HOME/app/models
mkdir $EZBUILD_HOME/app/db
mkdir $EZBUILD_HOME/app/assets
mkdir $EZBUILD_HOME/app/assets/javascripts
mkdir $EZBUILD_HOME/app/assets/stylesheets
mkdir $EZBUILD_HOME/conf/evolutions
mkdir $EZBUILD_HOME/conf/evolutions/default

while read -p 'Enter fully qualified path of require.js file to be copied to the play evolutions environment. ' REQUIREJS &&  [[ ! -f $REQUIREJS ]]; do
		echo "require.js not found at " $REQUIREJS
done

cp $REQUIREJS $EZBUILD_HOME/app/assets/javascripts


while read -p 'Enter fully qualified path of SQL file to be copied to the play evolutions environment. ' SQLFILE &&  [[ ! -f $SQLFILE ]]; do
		echo "require.js not found at " $SQLFILE
done


if [ $DB -eq 3 ]; then
	while read -p 'Enter fully qualified path of the Oracle ojdbc.jar file. ' ODJBCFILE &&  [[ ! -f $ODJBCFILE ]]; do
                echo "Oracle ojdbc file not found at " $ODJBCFILE
	done

	mkdir $EZBUILD_HOME/lib
	cp $ODJBCFILE $EZBUILD_HOME/lib
fi

cp $SQLFILE $EZBUILD_HOME/conf/evolutions/default



echo "package db" > $EZBUILD_HOME/app/db/Schema.scala
echo "" >> $EZBUILD_HOME/app/db/Schema.scala

echo "" >> $EZBUILD_HOME/app/db/Schema.scala 
echo 'object Schema {' >> $EZBUILD_HOME/app/db/Schema.scala
if  [ $DB -eq 1 ];  then
	echo '	val queryLanguage = slick.jdbc.MySQLProfile.api' >> $EZBUILD_HOME/app/db/Schema.scala
elif [ $DB -eq 2 ]; then
	echo '	val queryLanguage = slick.jdbc.PostgresProfile.api' >> $EZBUILD_HOME/app/db/Schema.scala
elif [ $DB -eq 3 ]; then
	echo '  val queryLanguage = slick.jdbc.OracleProfile.api' >> $EZBUILD_HOME/app/db/Schema.scala
fi
echo '	import queryLanguage._' >> $EZBUILD_HOME/app/db/Schema.scala
echo '	import slick.lifted.{Tag, TableQuery}' >> $EZBUILD_HOME/app/db/Schema.scala
echo '	// AUTOMATION TAG' >> $EZBUILD_HOME/app/db/Schema.scala
echo '}'  >> $EZBUILD_HOME/app/db/Schema.scala



if [ $DB -eq 1 ]; then
	echo 'slick.dbs.default.driver="slick.jdbc.MySQLProfile$"' >> $EZBUILD_HOME/conf/application.conf
#	echo 'slick.dbs.default.db.driver="com.mysql.jdbc.Driver"' >> $EZBUILD_HOME/conf/application.conf
	echo "slick.dbs.default.db.url=\"jdbc:mysql://$HOSTNAME:3306/$DBNAME?characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true\"" >> $EZBUILD_HOME/conf/application.conf
elif [ $DB -eq 2 ]; then
	echo 'slick.dbs.default.driver="slick.jdbc.PostgresProfile$"' >> $EZBUILD_HOME/conf/application.conf
#	echo 'slick.dbs.default.db.driver="org.postgresql.Driver"' >> $EZBUILD_HOME/conf/application.conf
	echo "slick.dbs.default.db.url=\"jdbc:postgresql://$HOSTNAME/$DBNAME\"" >> $EZBUILD_HOME/conf/application.conf
elif [ $DB -eq 3 ]; then
	echo 'slick.dbs.default.driver="slick.jdbc.OracleProfile$"' >> $EZBUILD_HOME/conf/application.conf
#	echo 'slick.dbs.default.db.driver="com.oracle.jdbc.OracleDriver"' >> $EZBUILD_HOME/conf/application.conf
	echo "slick.dbs.default.db.url=\"jdbc:oracle:thin:@$HOSTNAME:1521/$DBNAME\"" >> $EZBUILD_HOME/conf/application.conf
fi

echo 'slick.dbs.default.db.user='$USER >> $EZBUILD_HOME/conf/application.conf
echo 'slick.dbs.default.db.password='$PASSWORD >> $EZBUILD_HOME/conf/application.conf
echo 'jdbc-execution-context {' >> $EZBUILD_HOME/conf/application.conf
echo '	thread-pool-executor {' >> $EZBUILD_HOME/conf/application.conf
echo '	core-pool-size-factor = 10.0' >> $EZBUILD_HOME/conf/application.conf
echo '	core-pool-size-max = 10' >> $EZBUILD_HOME/conf/application.conf
echo '	}' >> $EZBUILD_HOME/conf/application.conf
echo '}' >> $EZBUILD_HOME/conf/application.conf
echo '' >> $EZBUILD_HOME/conf/application.conf
echo 'play.filters.hosts' { >> $EZBUILD_HOME/conf/application.conf
echo '	allowed = ["."]' >> $EZBUILD_HOME/conf/application.conf
echo '}' >> $EZBUILD_HOME/conf/application.conf
echo 'play.ws.cache.enabled=true' >> $EZBUILD_HOME/conf/application.conf

# built.sbt
echo 'libraryDependencies += evolutions' >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += filters' >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"' >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"' >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.1"' >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.26"'  >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += "com.zaxxer" % "HikariCP" % "3.3.1"' >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += jdbc'  >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += ws'  >> $EZBUILD_HOME/build.sbt
echo 'libraryDependencies += cache'  >> $EZBUILD_HOME/build.sbt

if [ $DB -eq 1 ]; then
	echo 'libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.15"' >> $EZBUILD_HOME/build.sbt
#	echo 'libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.22"' >> $EZBUILD_HOME/build.sbt
elif [ $DB -eq 2 ]; then
#	echo 'libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1100-jdbc4"' >> $EZBUILD_HOME/build.sbt
	echo 'libraryDependencies += "org.postgresql" % "postgresql" % "42.2.2"' >> $EZBUILD_HOME/build.sbt
elif [ $DB -eq 3 ]; then
	echo 'resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"' >> $EZBUILD_HOME/build.sbt
	echo 'libraryDependencies += "com.typesafe.slick" %% "slick-extensions" % "3.1.0"' >> $EZBUILD_HOME/build.sbt
fi

echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.2")' >> $EZBUILD_HOME/project/plugins.sbt
echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")' >> $EZBUILD_HOME/project/plugins.sbt
echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.6")' >> $EZBUILD_HOME/project/plugins.sbt
echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.10")' >> $EZBUILD_HOME/project/plugins.sbt
echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.3")' >> $EZBUILD_HOME/project/plugins.sbt
echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.2")' >> $EZBUILD_HOME/project/plugins.sbt
echo 'addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.2.2")' >> $EZBUILD_HOME/project/plugins.sbt
#echo 'addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")' >> $EZBUILD_HOME/project/plugins.sbt

echo "Completed"




