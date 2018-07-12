package tools.data.output;

import org.apache.mina.common.ByteBuffer;

public class ByteBufferOutputstream implements ByteOutputStream {
    private ByteBuffer bb;

    public ByteBufferOutputstream(final ByteBuffer bb) {
        super();
        this.bb = bb;
    }

    @Override
    public void writeByte(final byte b) {
        bb.put(b);
    }
}
