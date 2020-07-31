package com.teamfam.file.springscp.service;

import java.io.File;

import com.teamfam.file.springscp.transmit.FileTransmitter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ScpServiceImpl implements ScpService {

    @Autowired
    private FileTransmitter fileTrasmitter;

    @Override
    public Boolean scpFile(String fileName) {
        Assert.isTrue(StringUtils.isNoneBlank(fileName),"The file name cannot be blank.");
        File fileToTransmit = new File(fileName);
        return fileTrasmitter.transmit(fileToTransmit);
    }
    
}