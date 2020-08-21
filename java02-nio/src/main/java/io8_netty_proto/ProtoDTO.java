package io8_netty_proto;

import lombok.Data;

/**
 * @author zhuzhong@yunsom.com
 * @date 2020-08-21 14:04
 * @description
 */
@Data
public class ProtoDTO {

  private int length;

  private byte[] content;
}
