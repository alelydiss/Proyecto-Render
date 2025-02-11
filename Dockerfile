FROM ubuntu:latest
LABEL authors="Alejandro"

ENTRYPOINT ["top", "-b"]