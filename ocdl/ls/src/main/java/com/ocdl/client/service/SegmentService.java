package com.ocdl.client.service;

import java.io.File;

public interface SegmentService {

    /**
     * run segmentation model using command line
     * @param pictureName the name of picture that need to be segment
     * @return the output picture file
     */
    File run(String pictureName);
}
