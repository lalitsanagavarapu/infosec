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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * A data source implementation which can be used to read character files using
 * HTTP.
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
 * @version $Id: HttpDataSource.java 681182 2008-07-30 19:35:58Z shalin $
 * @since solr 1.3
 */
public class HttpDataSource extends DataSource<Reader> {
  Logger LOG = Logger.getLogger(HttpDataSource.class.getName());

  private String baseUrl;

  private String encoding;

  private int connectionTimeout = CONNECTION_TIMEOUT;

  private int readTimeout = READ_TIMEOUT;

  public HttpDataSource() {
  }

  public void init(Context context, Properties initProps) {
    baseUrl = initProps.getProperty(BASE_URL);
    if (initProps.get(ENCODING) != null)
      encoding = initProps.getProperty(ENCODING);
    String cTimeout = initProps.getProperty(CONNECTION_TIMEOUT_FIELD_NAME);
    String rTimeout = initProps.getProperty(READ_TIMEOUT_FIELD_NAME);
    if (cTimeout != null) {
      try {
        connectionTimeout = Integer.parseInt(cTimeout);
      } catch (NumberFormatException e) {
        LOG.log(Level.WARNING, "Invalid connection timeout: " + cTimeout);
      }
    }
    if (rTimeout != null) {
      try {
        readTimeout = Integer.parseInt(rTimeout);
      } catch (NumberFormatException e) {
        LOG.log(Level.WARNING, "Invalid read timeout: " + rTimeout);
      }
    }

  }

  public Reader getData(String query) {
    URL url = null;
    try {
      if (query.startsWith("http:")) {
        url = new URL(query);
      } else {
        url = new URL(baseUrl + query);
      }

      LOG.info("Created URL to: " + url.toString());

      URLConnection conn = url.openConnection();
      conn.setConnectTimeout(connectionTimeout);
      conn.setReadTimeout(readTimeout);
      InputStream in = conn.getInputStream();
      String enc = encoding;
      if (enc == null) {
        String cType = conn.getContentType();
        if (cType != null) {
          Matcher m = CHARSET_PATTERN.matcher(cType);
          if (m.find()) {
            enc = m.group(1);
          }
        }
      }
      if (enc == null)
        enc = UTF_8;
      DataImporter.QUERY_COUNT.get().incrementAndGet();
      return new InputStreamReader(in, enc);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "Exception thrown while getting data", e);
      throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
              "Exception in invoking url " + url, e);
    }
  }

  public void close() {
  }

  private static final Pattern CHARSET_PATTERN = Pattern.compile(
          ".*?charset=(.*)$", Pattern.CASE_INSENSITIVE);

  public static final String ENCODING = "encoding";

  public static final String BASE_URL = "baseUrl";

  public static final String UTF_8 = "UTF-8";

  public static final String CONNECTION_TIMEOUT_FIELD_NAME = "connectionTimeout";

  public static final String READ_TIMEOUT_FIELD_NAME = "readTimeout";

  public static final int CONNECTION_TIMEOUT = 5000;

  public static final int READ_TIMEOUT = 10000;
}
