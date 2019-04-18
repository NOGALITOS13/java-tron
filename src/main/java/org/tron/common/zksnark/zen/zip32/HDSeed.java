package org.tron.common.zksnark.zen.zip32;

import java.security.SecureRandom;
import lombok.Getter;
import lombok.Setter;
import org.tron.common.zksnark.zen.utils.CryptoGenerichashBlake2BState;
import org.tron.common.zksnark.zen.utils.PRF;

public class HDSeed {

  @Getter
  public RawHDSeed rawSeed;

  public static class RawHDSeed {

    @Getter
    @Setter
    public byte[] data;
  }

  public HDSeed() {
  }

  public HDSeed(byte[] data) {
    rawSeed = new RawHDSeed();
    rawSeed.data = data;
  }


  public void random(int len) {
    rawSeed = new RawHDSeed();

    byte[] bytes = new byte[len];
    new SecureRandom().nextBytes(bytes);

    rawSeed.data = bytes;
  }

  public byte[] ovkForShieldingFromTaddr() {
    CryptoGenerichashBlake2BState state = null;
    CryptoGenerichashBlake2BState
        .cryptoGenerichashBlake2BUpdate(state, rawSeed.data, rawSeed.data.length);
    byte[] intermediate = new byte[64];
    CryptoGenerichashBlake2BState.cryptoGenerichashBlake2BFinal(state, intermediate, 64);

    // I_L = I[0..32]
    byte[] intermediate_L = new byte[32];
    System.arraycopy(intermediate_L, 0, intermediate, 0, 32);

    // ovk = truncate_32(PRF^expand(I_L, [0x02]))
    return PRF.prfOvk(intermediate_L);
  }
}