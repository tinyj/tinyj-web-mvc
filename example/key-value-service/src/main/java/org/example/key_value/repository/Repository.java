/*
Copyright 2016 Eric Karge <e.karge@struction.de>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.example.key_value.repository;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class Repository {

  final static Random random = new Random();
  final Map<String, String> storage = new HashMap<>();

  public int count() {
    return storage.size();
  }

  public String createKey(String value) {
    final String key = createKey();
    storage.put(key, value);
    return key;
  }

  public String delete(String key) {
    return storage.remove(key);
  }

  public String get(String key) {
    final String value = storage.get(key);
    if (value == null) {
      throw new NoSuchElementException("Not found: " + key);
    }
    return value;
  }

  public Set<String> find(Set<String> keys, Set<String> values) {
    if (keys == null && values == null) {
      return storage.keySet();
    }
    return storage.entrySet().stream()
        .filter(e -> keys != null && keys.contains(e.getKey()))
        .filter(e -> values != null && values.contains(e.getValue()))
        .map(Map.Entry::getValue)
        .collect(toSet());
  }

  public String update(String key, String value) {
    storage.put(key, value);
    return value;
  }

  String createKey() {
    return Long.toHexString(random.nextLong());
  }
}
