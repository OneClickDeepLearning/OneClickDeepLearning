FROM python:3.7.2

WORKDIR /app

COPY . /app

RUN pip install --trusted-host pypi.python.org -r requirements.txt

EXPOSE 80

ENV NAME World

#CMD ["python", "app.py", "hello", "123"]

CMD python main.py "-f $FILEPATH" "-i $SOURCEIP" "-j"
