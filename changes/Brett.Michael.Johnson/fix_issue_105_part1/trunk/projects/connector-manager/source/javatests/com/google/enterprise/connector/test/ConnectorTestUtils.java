// Copyright (C) 2006-2008 Google Inc.
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

package com.google.enterprise.connector.test;

import com.google.enterprise.connector.manager.Context;
import com.google.enterprise.connector.servlet.ServletUtil;

import junit.framework.Assert;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConnectorTestUtils {

  private ConnectorTestUtils() {
    // prevents instantiation
  }

  /**
   * Find a named file on the classpath then read the entire content as a
   * String, skipping comment lines (lines that begin with #) and end-line
   * comments (from the first occurrence of // to the end).
   *
   * @param filename The name of file on the classpath
   * @return The contents of the reader (skipping comments)
   */
  public static String streamToString(String filename) {
    if (filename == null) {
      throw new IllegalArgumentException("filename is null");
    }
    if (filename.length() < 1) {
      throw new IllegalArgumentException("filename is empty");
    }
    InputStream s = ConnectorTestUtils.class.getResourceAsStream(filename);
    if (s == null) {
      throw new IllegalArgumentException(
          "filename must be found on the classpath: " + filename);
    }
    InputStreamReader isr = new InputStreamReader(s);
    BufferedReader br = new BufferedReader(isr);
    return streamToString(br);
  }

  /**
   * Read a buffered reader and return the entire contents as a String, skipping
   * comment lines (lines that begin with #) and end-line comments (from the
   * first occurrence of // to the end).
   *
   * @param br An Buffered Reader ready for reading
   * @return The contents of the reader (skipping comments)
   */
  public static String streamToString(BufferedReader br) {
    StringBuffer b = new StringBuffer(1024);
    String line;
    try {
      while ((line = br.readLine()) != null) {
        if (line.startsWith("#")) {
          // skip comment lines
          continue;
        }
        int index = line.indexOf("//");
        if (index == -1) {
          b.append(line);
        } else {
          b.append(line.subSequence(0, index));
        }
        b.append('\n');
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new String(b);
  }

  /**
   * Read an entire InputStream and return its contents as a String
   *
   * @param is InputStream to read
   * @return contents as a String
   */
  public static String streamToString(InputStream is) {
    byte buf[] = new byte[2048];
    int bytesRead;
    try {
      bytesRead = is.read(buf);
    } catch (IOException e) {
      throw new IllegalArgumentException("I/O problem reading stream");
    }
    String res = new String(buf, 0, bytesRead);
    return res;
  }

  /**
   * Normalizes strings with \r\n newlines to just \n
   *
   * @param input String to normalize
   * @return the normalized result
   */
  public static String normalizeNewlines(String input) {
    String result = input.replaceAll("\r\n", "\n");
    return result;
  }

  /**
   * Removes the connector manager version string from the buffer.
   * This allows the tests that compare actual output to expected
   * output to function across versions, jvms, and platforms.
   */
  public static void removeManagerVersion(StringBuffer buffer) {
    int start = buffer.indexOf("  <message>" + ServletUtil.MANAGER_NAME);
    if (start >= 0) {
      buffer.delete(start, buffer.indexOf("\n", start) + 1);
    }
  }

  /**
   * Removes the connector manager version string from the string.
   * This allows the tests that compare actual output to expected
   * output to function accross versions, jvms, and platforms.
   */
  public static String removeManagerVersion(String string) {
    StringBuffer buffer = new StringBuffer(string);
    removeManagerVersion(buffer);
    return buffer.toString();
  }

  /**
   * Gets the full path of a file by resolving it using the Context. This allows
   * there to be a different root directory if this is a junit test rather than
   * a servlet context.  For now, this routine is only for testing.
   *
   * @param fileName A relative file name to resolve
   * @param context The context
   * @return The full path name
   * @throws IOException
   */
  public static String getFileFullPath(String fileName, Context context)
      throws IOException {
    ApplicationContext applicationContext = context.getApplicationContext();
    Resource resource = applicationContext.getResource(fileName);
    File file = resource.getFile();
    String path = file.getAbsolutePath();
    return path;
  }

  public static void compareMaps(Map map1, Map map2, String map1name, String map2name) {
    Set set1 = map1.keySet();
    Set set2 = map2.keySet();
    Assert.assertTrue("there is a key in " + map2name + " that's not in "
        + map1name, set1.containsAll(set2));
    Assert.assertTrue("there is a key in " + map1name + " that's not in "
        + map2name, set2.containsAll(set1));

    for (Iterator i = set1.iterator(); i.hasNext();) {
      Object next = i.next();
      Assert.assertEquals(map1.get(next), map2.get(next));
    }
  }

  public static boolean deleteAllFiles(File dir) {
    if(!dir.exists()) {
        return true;
    }
    boolean res = true;
    if(dir.isDirectory()) {
        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++) {
            res &= deleteAllFiles(files[i]);
        }
        res = dir.delete();//Delete dir itself
    } else {
        res = dir.delete();
    }
    return res;
  }
}
