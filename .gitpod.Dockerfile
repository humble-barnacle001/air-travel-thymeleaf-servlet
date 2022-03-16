FROM gitpod/workspace-full

RUN bash -c "chmod +x /home/gitpod/.sdkman/bin/sdkman-init.sh && . /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.322-zulu"