package com.teamfam.file.springscp.transmit;

import java.io.File;
import java.io.FileInputStream;
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
        Boolean transmitted = Boolean.FALSE;
        try{
            currentSession = createSession();

        }catch(JSchException je){
            LOG.error("Unable to transmit the revstream file through SCP.",je);
        }finally{
            //disconnect session when there is an issue
            currentSession.disconnect();
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

        Session session = jsch.getSession(scpConfigurationProperties.getUserName(),scpConfigurationProperties.getHost(),Integer.parseInt(scpConfigurationProperties.getPort()));
        session.setConfig(sessionProps);
        session.connect();
        return session;
    }

    private boolean scpLocalToRemote(Session session, File from, String to, String fileName) throws JSchException, IOException {
        boolean ptimestamp = true;
        boolean transmitted = Boolean.FALSE;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + to;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        if (ptimestamp) {
            command = "T" + (from.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (from.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = from.length();
        command = "C0644 " + filesize + " ";
        if (from.getAbsolutePath().lastIndexOf('/') > 0) {
            command += from.getAbsolutePath().substring(from.getAbsolutePath().lastIndexOf('/') + 1);
        } else {
            command += from.getAbsolutePath();
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // send a content of lfile
        FileInputStream fis = new FileInputStream(from);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        try {
            if (fis != null) fis.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        transmitted = Boolean.TRUE;
        channel.disconnect();
        session.disconnect();

        return transmitted;
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
                LOG.error(sb.toString());
            }
        }
        return ackByte;
    }    
}