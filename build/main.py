import sys, getopt, os
import json
from flask import Flask
from model_sklearn_adaptor import ModelSklearnAdaptor


app = Flask(__name__)


@app.route('/predict', methods=['POST'])
def predict_interface():
	global model
	in_str = request.files['i']
	out = model.predict(json.loads(in_str))

	return json.dumps(out)


if __name__ == '__main__':
	opts, args = getopt.getopt(sys.argv[1:], "f:i:j")
	model_file_path = None
	private_ip_address = "0.0.0.0"
	is_json_asc2 = False

	# load params
	for op, value in opts:
	    if op == "-f":
	        model_file_path = value.strip()
	    elif op == "-i":
	        private_ip_address = value
	    elif op == "-j":
	    	is_json_asc2 = True

	# check params
	if model_file_path == None:
		sys.exit()
	
	print(model_file_path)

	model_format = model_file_path.split('.')[1]

	if model_format == 'joblib':
		model = ModelSklearnAdaptor()
	elif model_format == '':
		pass

	os.getcwd()

	model.load("ds2_dt_classifier.joblib")
	app.config['JSON_AS_ASCII'] = is_json_asc2
	app.run(host=private_ip_address)
