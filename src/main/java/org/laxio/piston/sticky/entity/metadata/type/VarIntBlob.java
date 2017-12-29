package org.laxio.piston.sticky.entity.metadata.type;

import org.laxio.piston.sticky.entity.metadata.PistonMetadataBlob;

public class VarIntBlob extends PistonMetadataBlob<Integer> {

    public VarIntBlob(int index, Integer value) {
        super(index, value);
    }

}
