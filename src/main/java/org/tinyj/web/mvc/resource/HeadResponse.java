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
package org.tinyj.web.mvc.resource;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Objects;

public class HeadResponse extends HttpServletResponseWrapper {

  private OutStream stream;
  private PrintWriter writer;

  public HeadResponse(HttpServletResponse response) {
    super(response);
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (writer != null) {
      throw new IllegalStateException("getOutputStream() called after getWriter()");
    }
    if (stream != null) {
      return stream;
    }
    return stream = new OutStream();
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    if (writer != null) {
      return writer;
    }
    if (stream != null) {
      throw new IllegalStateException("getWriter() called after getOutputStream()");
    }
    setCharacterEncoding(getCharacterEncoding());
    return writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
  }

  @Override
  public void flushBuffer() throws IOException {
    super.flushBuffer();
    if (stream != null && stream.isReady()) {
      if (writer != null) {
        writer.flush();
      } else {
        stream.flush();
      }
      return;
    }
    super.flushBuffer();
  }

  @Override
  public void reset() {
    super.reset();
    stream = null;
    writer = null;
  }

  @Override
  public void resetBuffer() {
    super.resetBuffer();
    if (stream != null) {
      stream.count = 0;
    }
  }

  public void close() throws IOException {
    if (stream != null && stream.isReady()) {
      if (writer != null) {
        writer.close();
      } else {
        stream.close();
      }
      return;
    }
    if (!isCommitted()) {
      if (getHeader("Content-Length") == null
          && !Objects.equals(getHeader("Transfer-Encoding"), "identity")) {
        setContentLength(stream != null ? stream.count : 0);
      }
    }
    super.flushBuffer();
  }

  private class OutStream extends ServletOutputStream {

    public int count;
    private boolean ready = true;

    @Override
    public boolean isReady() {
      return ready;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
      try {
        if (isReady()) {
          writeListener.onWritePossible();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void write(int b) throws IOException {
      requireReady();
      ++count;
    }

    @Override
    public void write(byte[] b) throws IOException {
      requireReady();
      count += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      requireReady();
      count += len;
    }

    @Override
    public void flush() throws IOException {
      if (isReady()) {
        ready = false;
        flushBuffer();
        ready = true;
      }
    }

    @Override
    public void close() throws IOException {
      if (isReady()) {
        ready = false;
        HeadResponse.this.close();
      }
    }

    protected void requireReady() {
      if (!isReady()) {
        throw new IllegalStateException();
      }
    }
  }
}
