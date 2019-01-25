source read.sh

docker build -t test . && 

docker run -e FILEPATH="$FILEPATH" -e SOURCEIP="$SOURCEIP" test ||

echo "Failed"
