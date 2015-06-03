/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.hdfsdb;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Iterator extends NativeObject implements java.util.Iterator<Map
        .Entry<byte[], byte[]>> {

  Iterator(long nativeHandle) {
    super(nativeHandle);
  }

  public void seek(byte[] key) {
    seek(nativeHandle, key);
  }

  @Override
  public boolean hasNext() {
    return valid(nativeHandle);
  }

  @Override
  public Map.Entry<byte[], byte[]> next() {
    Map.Entry<byte[], byte[]> res = peekNext();
    next(nativeHandle);
    return res;
  }

  public Map.Entry<byte[], byte[]> peekNext() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    return new AbstractMap.SimpleImmutableEntry<byte[], byte[]>(key
            (nativeHandle), value(nativeHandle));
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    if (nativeHandle != 0) {
      destruct(nativeHandle);
      nativeHandle = 0;
    }
  }

  private static native void destruct(long handle);
  private static native void seek(long handle, byte[] key);
  private static native void next(long handle);
  private static native boolean valid(long handle);
  private static native byte[] key(long handle);
  private static native byte[] value(long handle);
}