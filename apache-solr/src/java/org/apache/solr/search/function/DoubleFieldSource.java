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

package org.apache.solr.search.function;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ExtendedFieldCache;

import java.io.IOException;

/**
 * Obtains float field values from the {@link org.apache.lucene.search.FieldCache}
 * using <code>getFloats()</code>
 * and makes those values available as other numeric types, casting as needed.
 *
 * @version $Id:$
 */

public class DoubleFieldSource extends FieldCacheSource {
  protected ExtendedFieldCache.DoubleParser parser;

  public DoubleFieldSource(String field) {
    this(field, null);
  }

  public DoubleFieldSource(String field, ExtendedFieldCache.DoubleParser parser) {
    super(field);
    this.parser = parser;
  }

  public String description() {
    return "double(" + field + ')';
  }

  public DocValues getValues(IndexReader reader) throws IOException {
    final double[] arr = (parser == null) ?
            ((ExtendedFieldCache) cache).getDoubles(reader, field) :
            ((ExtendedFieldCache) cache).getDoubles(reader, field, parser);
    return new DocValues() {
      public float floatVal(int doc) {
        return (float) arr[doc];
      }

      public int intVal(int doc) {
        return (int) arr[doc];
      }

      public long longVal(int doc) {
        return (long) arr[doc];
      }

      public double doubleVal(int doc) {
        return arr[doc];
      }

      public String strVal(int doc) {
        return Double.toString(arr[doc]);
      }

      public String toString(int doc) {
        return description() + '=' + floatVal(doc);
      }
    };
  }

  public boolean equals(Object o) {
    if (o.getClass() != DoubleFieldSource.class) return false;
    DoubleFieldSource other = (DoubleFieldSource) o;
    return super.equals(other)
            && this.parser == null ? other.parser == null :
            this.parser.getClass() == other.parser.getClass();
  }

  public int hashCode() {
    int h = parser == null ? Float.class.hashCode() : parser.getClass().hashCode();
    h += super.hashCode();
    return h;
  }

  ;

}
