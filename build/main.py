import sys, getopt
from flask import Flask
from model_sklearn_adaptor import ModelSklearnAdaptor
# from model_keras_adaptor import ModelKerasAdaptor


app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict_interface():
	global model
	in_str = request.files['i']
	out = model.predict(model.deserialize(in_str))
	return model.serialize(out)


if __name__ == '__main__':
	opts, args = getopt.getopt(sys.argv[1:], "f:i:j")
	model_file_path = None
	private_ip_address = "0.0.0.0"
	is_json_asc2 = False

	# load params
	for op, value in opts:
	    if op == "-f":
	        model_file_path = value
	    elif op == "-i":
	        private_ip_address = value
	    elif op == "-j":
	    	is_json_asc2 = True


	# test
	model_file_path = "ds2_dt_classifier.joblib"
	private_ip_address = "0.0.0.0"

	# check params
	if model_file_path == None:
		sys.exit()

	# initialize
	models = list()
	model_sklearn = ModelSklearnAdaptor()
	# model_keras = ModelKerasAdaptor()
	models.append(model_sklearn)
	# models.append(model_keras)

	# model selection
	model_format = model_file_path.split('.')[1]
	model = None
	for m in models:
		if m.file_postfix() == model_format:
			model = m
			break


	if model != None:
		model.load(model_file_path)
		app.config['JSON_AS_ASCII'] = is_json_asc2
		app.run(host=private_ip_address)
    
	sys.exit()