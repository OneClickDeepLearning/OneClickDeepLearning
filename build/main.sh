source read.sh

docker build -t hello . && 

docker run -e FILEPATH="$FILEPATH" -e NUMBER="$NUMBER" -e SOURCEIP="$SOURCEIP" hello ||

echo "Failed"
