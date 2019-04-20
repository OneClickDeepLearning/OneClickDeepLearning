package com.ocdl.client.service;

public interface SegmentService {

    /**
     * run segmentation model using command line
     * @param pictureName the name of picture that need to be segment
     * @return the output picture name
     */
    String run(String pictureName);
}
