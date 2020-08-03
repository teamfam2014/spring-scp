#Docker of SSH server with Open SSH installed and initialized with 
#ssh auth keys created and executed.
FROM alpine:latest
RUN apk upgrade && \
    apk update && \
    apk add openssh && \
    rm /var/cache/apk/* && \
    mkdir /root/.ssh && \
    chmod 700 /root/.ssh && \    
    mkdir -p /tmp
COPY ssh_host_rsa_key /etc/ssh/
COPY ssh_host_rsa_key.pub /etc/ssh/
COPY sshd_config /etc/ssh/
COPY authorized_keys /root/.ssh/
RUN chmod 600 /etc/ssh/ssh_*_key /root/.ssh/authorized_keys    
RUN passwd -u root    
ENTRYPOINT ["/usr/sbin/sshd", "-D", "-e"]