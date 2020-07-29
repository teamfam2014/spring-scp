package com.teamfam.file.springscp.transmit;

import java.io.File;

/**
 * Transmit a file.
 * 
 * @author teamfam
 */
public interface FileTransmitter {
    
    /**
     * <p>Send the <b>fileToTransmit</b> across any medium and through any transmission
     * protocol.</p>
     * @param fileToTransmit The file to send across.
     * @return Boolean.TRUE if the file was successfully sent, Boolean.FALSE otherwise.
     */
    public Boolean transmit(File fileToTransmit);
}