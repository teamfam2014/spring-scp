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
RUN passwd -u root    
ENTRYPOINT ["/usr/sbin/sshd", "-D", "-e"]