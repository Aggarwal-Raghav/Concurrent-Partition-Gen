# Description

The project is a java program that created empty directories in the hdfs concurrently. This is helpful to create large number of partitions for hive tables.
Post generation, user can run ``` msck repair table <table_name>``` to add the partitions to the hive metastore.

## To build the project:

```bash
mvn clean install
```

## To use the jar:

```bash
java -cp target/Concurrent-Partition-Gen-1.0-SNAPSHOT-jar-with-dependencies.jar com.github.raghav.Main
```

