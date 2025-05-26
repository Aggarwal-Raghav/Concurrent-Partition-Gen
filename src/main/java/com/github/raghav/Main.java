/*
 * Copyright 2025 Raghav Aggarwal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.raghav;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Main {

  public static final Configuration CONF = setConf();
  public static final FileSystem FS = evalFs();

  private static Configuration setConf() {
    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "hdfs://localhost:9000");
    conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
    conf.set("hadoop.security.authentication", "simple");

    /*
    System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
    System.setProperty("java.security.krb5.realm", "ORG.COM");
    System.setProperty("java.security.krb5.kdc", "localhost:88");
    conf.set("hadoop.security.authentication", "kerberos");
    conf.set("hadoop.security.authorization", "true");
    conf.set("dfs.namenode.kerberos.principal", "nn/_HOST@ORG.COM");
    conf.set("dfs.namenode.keytab.file", "/etc/security/keytabs/hdfs.headless.keytab");
    conf.set("dfs.datanode.kerberos.principal", "dn/_HOST@ORG.COM");
    conf.set("dfs.datanode.keytab.file", "/etc/security/keytabs/hdfs.headless.keytab");

    UserGroupInformation.setConfiguration(conf);
    try {
      UserGroupInformation.loginUserFromKeytab(
          "hdfs-optimus@ORG.COM", "/etc/security/keytabs/hdfs.headless.keytab");
    } catch (Exception e) {
      throw new RuntimeException("Failed to login with keytab", e);
    }
    */

    return conf;
  }

  private static FileSystem evalFs() {
    final String HDFS_URI = "hdfs://localhost:9000/";
    FileSystem fs = null;
    try {
      fs = FileSystem.get(new Path(HDFS_URI).toUri(), CONF);
    } catch (IOException e) {
      throw new RuntimeException("Failed to get FileSystem instance", e);
    }
    return fs;
  }

  public static void main(String[] args) throws InterruptedException {
    int threadCount = 8;
    int partitionCount = 5000;
    int countPerThread = partitionCount / threadCount;
    List<DirectoryThread> lsThread =
        IntStream.range(0, threadCount)
            .mapToObj(j -> new DirectoryThread(j * countPerThread, countPerThread))
            .toList();

    for (var thread : lsThread) {
      thread.start();
    }

    for (var thread : lsThread) {
      thread.join();
    }
  }
}
