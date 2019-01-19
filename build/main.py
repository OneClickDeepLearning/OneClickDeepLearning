import sys, getopt
from flask import Flask

opts, args = getopt.getopt(sys.argv[1:], "f:i:n:")

model_file_path = None
private_ip_address = "0.0.0.0"
number_thread = 4

# load params
for op, value in opts:
    if op == "-f":
        model_file_path = value
    elif op == "-i":
        private_ip_address = value
    elif op == "-n":
    	number_thread = int(str.strip(value))

# check params
if model_file_path == None:
	sys.exit()

print(model_file_path)
print(private_ip_address)
print(number_thread )


