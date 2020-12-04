package io8_netty_proto;

import lombok.Data;

/**
 * @author farmer.zs@qq.com
 * @date 2020-08-21 14:04
 * @description
 */
@Data
public class ProtoDTO {

  private int length;

  private byte[] content;
}
