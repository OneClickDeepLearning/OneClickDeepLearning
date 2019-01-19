source read.sh

docker build -t test . && 

docker run -e FILEPATH="$FILEPATH" -e SOURCEIP="$SOURCEIP" -e NUMBER="$NUMBER" test ||

echo "Failed"
