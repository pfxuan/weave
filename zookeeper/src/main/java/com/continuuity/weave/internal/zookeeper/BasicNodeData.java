/*
 * Copyright 2012-2013 Continuuity,Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.continuuity.weave.internal.zookeeper;

import com.continuuity.weave.zookeeper.NodeData;
import com.google.common.base.Objects;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;

/**
 * A straightforward implementation for {@link NodeData}.
 */
final class BasicNodeData implements NodeData {

  private final byte[] data;
  private final Stat stat;

  BasicNodeData(byte[] data, Stat stat) {
    this.data = data;
    this.stat = stat;
  }

  @Override
  public Stat getStat() {
    return stat;
  }

  @Override
  public byte[] getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof NodeData)) {
      return false;
    }

    BasicNodeData that = (BasicNodeData) o;

    return stat.equals(that.getStat()) && Arrays.equals(data, that.getData());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(data, stat);
  }
}
