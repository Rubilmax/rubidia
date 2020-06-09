FROM python:3.7

RUN mkdir /app

WORKDIR /app

COPY install.sh .
RUN chmod +x install.sh && ./install.sh

COPY requirements.txt .
RUN pip install -r requirements.txt