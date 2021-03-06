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

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * An EntityProcessor instance which provides support for reading from
 * databases. It is used in conjunction with JdbcDataSource. This is the default
 * EntityProcessor if none is specified explicitly in data-config.xml
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
 * @version $Id: SqlEntityProcessor.java 681182 2008-07-30 19:35:58Z shalin $
 * @since solr 1.3
 */
public class SqlEntityProcessor extends EntityProcessorBase {
  private static final Logger LOG = Logger.getLogger(SqlEntityProcessor.class
          .getName());

  protected DataSource<Iterator<Map<String, Object>>> dataSource;

  @SuppressWarnings("unchecked")
  public void init(Context context) {
    super.init(context);
    dataSource = context.getDataSource();
  }

  protected void initQuery(String q) {
    try {
      DataImporter.QUERY_COUNT.get().incrementAndGet();
      rowIterator = dataSource.getData(q);
      this.query = q;
    } catch (DataImportHandlerException e) {
      throw e;
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "The query failed '" + q + "'", e);
      throw new DataImportHandlerException(DataImportHandlerException.SEVERE, e);
    }
  }

  public Map<String, Object> nextRow() {
    if (rowcache != null)
      return getFromRowCache();
    if (rowIterator == null) {
      String q = getQuery();
      initQuery(resolver.replaceTokens(q));
    }
    while (true) {
      Map<String, Object> r = getNext();
      if (r == null)
        return null;
      r = applyTransformer(r);
      if (r != null)
        return r;
    }

  }

  public Map<String, Object> nextModifiedRowKey() {
    if (rowIterator == null) {
      String deltaQuery = context.getEntityAttribute(DELTA_QUERY);
      if (deltaQuery == null)
        return null;
      initQuery(resolver.replaceTokens(deltaQuery));
    }
    return getNext();
  }

  public Map<String, Object> nextDeletedRowKey() {
    if (rowIterator == null) {
      String deletedPkQuery = context.getEntityAttribute(DEL_PK_QUERY);
      if (deletedPkQuery == null)
        return null;
      initQuery(resolver.replaceTokens(deletedPkQuery));
    }
    return getNext();
  }

  public Map<String, Object> nextModifiedParentRowKey() {
    if (rowIterator == null) {
      String parentDeltaQuery = context.getEntityAttribute(PARENT_DELTA_QUERY);
      if (parentDeltaQuery == null)
        return null;
      LOG.info("Running parentDeltaQuery for Entity: "
              + context.getEntityAttribute("name"));
      initQuery(resolver.replaceTokens(parentDeltaQuery));
    }
    return getNext();
  }

  public String getQuery() {
    String queryString = context.getEntityAttribute(QUERY);
    if (context.currentProcess() == Context.FULL_DUMP
            || !context.isRootEntity()) {
      return queryString;
    }
    return getDeltaImportQuery(queryString);
  }

  public String getDeltaImportQuery(String queryString) {
    StringBuffer sb = new StringBuffer(queryString);
    if (SELECT_WHERE_PATTERN.matcher(queryString).find()) {
      sb.append(" and ");
    } else {
      sb.append(" where ");
    }
    boolean first = true;
    String[] primaryKeys = context.getEntityAttribute("pk").split(",");
    for (String primaryKey : primaryKeys) {
      if (!first) {
        sb.append(" and ");
      }
      first = false;
      Object val = resolver.resolve("dataimporter.delta." + primaryKey);
      if (val == null) {
        Matcher m = DOT_PATTERN.matcher(primaryKey);
        if (m.find()) {
          val = resolver.resolve("dataimporter.delta." + m.group(1));
        }
      }
      sb.append(primaryKey).append(" = ");
      if (val instanceof Number) {
        sb.append(val.toString());
      } else {
        sb.append("'").append(val.toString()).append("'");
      }
    }
    return sb.toString();
  }

  private static Pattern SELECT_WHERE_PATTERN = Pattern.compile(
          "^\\s*(select\\b.*?\\b)(where).*", Pattern.CASE_INSENSITIVE);

  public static final String QUERY = "query";

  public static final String DELTA_QUERY = "deltaQuery";

  public static final String PARENT_DELTA_QUERY = "parentDeltaQuery";

  public static final String DEL_PK_QUERY = "deletedPkQuery";

  public static final Pattern DOT_PATTERN = Pattern.compile(".*?\\.(.*)$");
}
