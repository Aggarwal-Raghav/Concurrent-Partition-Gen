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
import org.apache.hadoop.fs.Path;

public class DirectoryThread extends Thread {
  private int from;
  private int count;
  private static final String BASE_PATH = "/user/hive/warehouse/test_db.db/test_tbl/part_col=pc";

  public DirectoryThread(int from, int count) {
    this.from = from;
    this.count = count;
  }

  @Override
  public void run() {
    for (int i = from; i < from + count; i++) {
      Path path = new Path(BASE_PATH + i);
      try {
        Main.FS.mkdirs(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
