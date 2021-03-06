/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.handler.dataimport;

import java.io.*;
import java.util.Properties;

/**
 * <p>
 * A DataSource which reads from local files
 * </p>
 * <p>
 * The file is read with the default platform encoding. It can be overriden by
 * specifying the encoding in solrconfig.xml
 * </p>
 * <p/>
 * <p>
 * Refer to <a
 * href="http://wiki.apache.org/solr/DataImportHandler">http://wiki.apache.org/solr/DataImportHandler</a>
 * for more details.
 * </p>
 * <p/>
 * <b>This API is experimental and may change in the future.</b>
 *
 * @version $Id: FileDataSource.java 681182 2008-07-30 19:35:58Z shalin $
 * @since solr 1.3
 */
public class FileDataSource extends DataSource<Reader> {
  public static final String BASE_PATH = "basePath";

  private String basePath;

  private String encoding = null;

  public void init(Context context, Properties initProps) {
    basePath = initProps.getProperty(BASE_PATH);
    if (initProps.get(HttpDataSource.ENCODING) != null)
      encoding = initProps.getProperty(HttpDataSource.ENCODING);
  }

  /**
   * <p>
   * Returns a reader for the given file.
   * </p>
   * <p>
   * If the given file is not absolute, we try to construct an absolute path
   * using basePath configuration. If that fails, then the relative path is
   * tried. If file is not found a RuntimeException is thrown.
   * </p>
   * <p>
   * <b>It is the responsibility of the calling method to properly close the
   * returned Reader</b>
   * </p>
   */
  public Reader getData(String query) {
    try {
      File file0 = new File(query);
      File file = file0;

      if (!file.isAbsolute())
        file = new File(basePath + query);

      if (file.isFile() && file.canRead()) {
        return openStream(file);
      } else if (file != file0)
        if (file0.isFile() && file0.canRead())
          return openStream(file0);

      throw new FileNotFoundException("Could not find file: " + query);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private InputStreamReader openStream(File file) throws FileNotFoundException,
          UnsupportedEncodingException {
    if (encoding == null) {
      return new InputStreamReader(new FileInputStream(file));
    } else {
      return new InputStreamReader(new FileInputStream(file), encoding);
    }
  }

  public void close() {

  }
}
