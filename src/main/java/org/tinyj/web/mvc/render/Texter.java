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
package org.tinyj.web.mvc.render;

import java.io.Reader;
import java.io.Writer;
import java.util.function.Function;

/**
 * Intended to be used in a functor style. I.e. an implementation usually
 * carries data to write into a `java.io.Writer`.
 */
@FunctionalInterface
public interface Texter {

  /**
   * Write data to `writer`. `writer` should be ready to be written to.
   * By contract an implementation is required to not call `writer.close()`
   * on invocation.
   */
  void writeTo(Writer writer) throws Exception;

  /** Creates a `Texter` that writes `toWrite.toString()` on invocation. */
  static <T> Texter text(T toWrite) {
    return text(toWrite, Object::toString);
  }

  /**
   * Creates a `Texter` that, when invoked, writes `toWrite` converted to
   * string using `stringifier`.
   */
  static <T> Texter text(T toWrite, Function<? super T, String> stringifier) {
    return writer -> writer.write(stringifier.apply(toWrite));
  }

  /** Creates a `Texter` that writes all text from `reader` on invocation. */
  static Texter text(Reader reader) {
    return text(reader, 4096);
  }

  static Texter text(Reader reader, int bufferSize) {
    return writer -> {
      char[] buffer = new char[bufferSize];
      int read;
      while ((read = reader.read(buffer)) >= 0) {
        writer.write(buffer, 0, read);
      }
    };
  }
}
