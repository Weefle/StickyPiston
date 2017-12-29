package org.laxio.piston.sticky.entity.metadata.type;

import org.laxio.piston.piston.entity.metadata.MetadataBlob;
import org.laxio.piston.piston.exception.entity.metadata.EndOfMetadataException;
import org.laxio.piston.piston.exception.entity.metadata.UnknownMetadataBlobException;
import org.laxio.piston.piston.protocol.stream.PistonInput;
import org.laxio.piston.piston.protocol.stream.PistonOutput;
import org.laxio.piston.sticky.entity.metadata.BlobFactory;
import org.laxio.piston.sticky.entity.metadata.type.factory.ByteBlobFactory;

import java.io.IOException;

public enum BlobType {

    BYTE(0x00, new ByteBlobFactory());

    private int type;
    private BlobFactory factory;

    BlobType(int type, BlobFactory factory) {
        this.type = type;
        this.factory = factory;
    }

    public int getType() {
        return type;
    }

    public BlobFactory getFactory() {
        return factory;
    }

    public static MetadataBlob read(PistonInput input) throws IOException, EndOfMetadataException, UnknownMetadataBlobException {
        int index = input.readUnsignedByte();
        if (index == 0xFF) {
            throw new EndOfMetadataException();
        }

        int type = input.readByte();
        for (BlobType blob : values()) {
            if (blob.type == type) {
                return blob.factory.read(input, index);
            }
        }

        throw new UnknownMetadataBlobException("No such blob type for " + type, type);
    }

    @SuppressWarnings("unchecked")
    public static void write(PistonOutput output, MetadataBlob blob) throws IOException, UnknownMetadataBlobException {
        for (BlobType type : values()) {
            if (type.factory.matches(blob)) {
                type.factory.write(output, blob);
                return;
            }
        }

        throw new UnknownMetadataBlobException("Unable to find factory matching " + blob.getClass().getSimpleName(), -1);
    }

}
