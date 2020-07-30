package com.teamfam.file.springscp.transmit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.teamfam.file.springscp.config.ScpConfigurationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Transmit a file through SCP.
 * 
 * @author teamfam
 */
@Component
public class ScpFileTransmitter implements FileTransmitter {

    private static final Logger LOG = LoggerFactory.getLogger(ScpFileTransmitter.class);

    @Autowired
    private JSch jsch;

    @Autowired
    private ScpConfigurationProperties scpConfigurationProperties;

    @Override
    public Boolean transmit(File fileToTransmit) {
        Session currentSession = null;
        Channel currentChannel = null;
        OutputStream out = null;
        InputStream in = null;
        Boolean transmitted = Boolean.FALSE;
        try{
            currentSession = createSession();
            currentChannel = getChannel(currentSession,scpConfigurationProperties.getRemoteFilePath());
            // get I/O streams for remote scp
            out = currentChannel.getOutputStream();
            in = currentChannel.getInputStream();
            currentChannel.connect();
            transmitted = scpLocalToRemote(out,in,fileToTransmit);
        }catch(Throwable t){
            LOG.error("Unable to transmit the file through SCP.",t);
        }finally{
            //close output stream
            if(out != null){
                try{
                    LOG.debug("Closing OutputStream.");
                    out.close();
                    LOG.debug("OutputStream Closed.");
                }catch(IOException ioe){
                    LOG.error("Unable to close output stream.",ioe);
                }
            }
            //close input stream
            if(in != null){
                try{
                    LOG.debug("Closing InputStream.");
                    in.close();
                    LOG.debug("InputStream Closed.");
                }catch(IOException ioe){
                    LOG.error("Unable to close input stream.",ioe);
                }
            }            
            //close channel
            if (currentChannel != null){
                LOG.debug("Closing JSCH Channel.");
                currentChannel.disconnect();
                LOG.debug("JSCH Channel closed.");
            }
            //close session
            if (currentSession != null){
                LOG.debug("Closing from Session.");
                currentSession.disconnect();
                LOG.debug("Session closed.");
            }            
        }
        return transmitted;
    }
    
    private Session createSession() throws JSchException{
        Assert.notNull(scpConfigurationProperties.getSessionConfigs(),"The session configuration cannot be null.");
        Assert.isTrue(!scpConfigurationProperties.getSessionConfigs().isEmpty(),"The session configuration cannot be empty.");
        //populate the session properties
        Properties sessionProps = new Properties();
        scpConfigurationProperties.getSessionConfigs().forEach((sessionPropKey,sessionPropVal) -> {
            sessionProps.put(sessionPropKey, sessionPropVal);
        });
        LOG.info("Establishing Session.");
        Session session = jsch.getSession(scpConfigurationProperties.getUserName(),scpConfigurationProperties.getHost(),Integer.parseInt(scpConfigurationProperties.getPort()));
        session.setConfig(sessionProps);
        session.connect();
        LOG.info("Session Established.");
        return session;
    }

    private Channel getChannel(Session session, String remoteFilePath) throws JSchException{
                // exec 'scp -t rfile' remotely
                String command = "scp " + " -t " + remoteFilePath;
                LOG.debug("Opening JSCH Channel. command=\"{}\"",command);
                Channel channel = session.openChannel("exec");
                LOG.debug("JSCH Channel Open. command=\"{}\"",command);
                ((ChannelExec) channel).setCommand(command);
                return channel;
    }

    private boolean scpLocalToRemote(OutputStream out, InputStream in, File localFile) throws JSchException, IOException {
        LOG.info("Preparing SCP Transmission. fileName={}",localFile.getName());
        boolean transmitted = Boolean.FALSE;
        sendFileName(localFile,out);

        if (checkAck(in) != 0) {
            return transmitted;
        }

        sendFileContents(localFile, out);

        if (checkAck(in) != 0) {
            return transmitted;
        }

        transmitted = Boolean.TRUE;
        LOG.info("SCP Transmission Complete. fileName={} | transmitted={}",localFile.getName(),transmitted);
        return transmitted;
    }

    /**
     * Send "C0644 filesize filename", where filename should not include '/'
     **/
    private void sendFileName(File localFile, OutputStream out) throws IOException{
        long filesize = localFile.length();
        String command = "C0644 " + filesize + " ";
        if (localFile.getAbsolutePath().lastIndexOf('/') > 0) {
            command += localFile.getAbsolutePath().substring(localFile.getAbsolutePath().lastIndexOf('/') + 1);
        } else {
            command += localFile.getAbsolutePath();
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();
    }

    private void sendFileContents(File localFile,OutputStream out) throws IOException{
        // send a content of local file
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(localFile);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len);
            }
    
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
        }catch(FileNotFoundException fnfe){
            LOG.error("File Not Found to send.",fnfe);
        }finally{
            if (fis != null){
                fis.close();
            } 
        }
    }

    private int checkAck(InputStream in) throws IOException {
        int ackByte = in.read();
        // ackByte may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (ackByte == 0) return ackByte;
        if (ackByte == -1) return ackByte;

        if (ackByte == 1 || ackByte == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (ackByte == 1 || ackByte == 2) { // error
                LOG.error("The file stream contains the following acknowledgements. in={}", sb.toString());
            }
        }
        return ackByte;
    }    
}