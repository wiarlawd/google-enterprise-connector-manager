// Copyright (C) 2006 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.connector.spi;

import java.util.Iterator;

/**
 * A handle to a query result set.
 */
public interface PropertyMapList {

  /**
   * Produces an iterator through which query results can be explored. Each
   * result is a {@link PropertyMap}. This method should only be called once. 
   * @return Iterator of {@link PropertyMap} objects
   * @throws RepositoryException if something is wrong with the repository, 
   * such as no connectivity
   */
  public Iterator iterator() throws RepositoryException;

}