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

package org.apache.solr.core;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.SolrIndexSearcher;

import java.util.logging.Logger;

/**
 * @version $Id: SolrEventListener.java 681447 2008-07-31 19:27:36Z yonik $
 */
public interface SolrEventListener {
  static final Logger log = Logger.getLogger(SolrCore.class.getName());

  public void init(NamedList args);

  public void postCommit();

  /** The searchers passed here are only guaranteed to be valid for the duration
   * of this method call, so care should be taken not to spawn threads or asynchronous
   * tasks with references to these searchers.
   */
  public void newSearcher(SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher);

}
