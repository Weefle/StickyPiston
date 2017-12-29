package org.laxio.piston.sticky.entity.metadata;

import org.laxio.piston.piston.entity.metadata.MetadataBlob;

public class PistonMetadataBlob<T> implements MetadataBlob<T> {

    private final int index;
    private final T value;

    public PistonMetadataBlob(int index, T value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public T getValue() {
        return value;
    }

}
