package org.laxio.piston.sticky.entity.metadata.type.factory;

import org.laxio.piston.piston.entity.metadata.MetadataBlob;
import org.laxio.piston.piston.protocol.stream.PistonInput;
import org.laxio.piston.piston.protocol.stream.PistonOutput;
import org.laxio.piston.sticky.entity.metadata.BlobFactory;
import org.laxio.piston.sticky.entity.metadata.type.ByteBlob;

import java.io.IOException;

public class ByteBlobFactory extends BlobFactory<ByteBlob> {

    @Override
    public boolean matches(MetadataBlob blob) {
        return blob instanceof ByteBlob;
    }

    @Override
    public ByteBlob read(PistonInput input, int index) throws IOException {
        return new ByteBlob(index, input.readByte());
    }

    @Override
    public void write(PistonOutput output, ByteBlob blob) throws IOException {
        output.writeByte(blob.getValue());
    }

}