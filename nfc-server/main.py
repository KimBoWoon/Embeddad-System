from flask import Flask
from sqlalchemy import create_engine
from db import DBManager

app = Flask(__name__)

def databaseConnect():
	DBManager.init('mysql+pymysql://root:rlaqhdns@localhost/nfc?charset=utf8', True)
	DBManager.init_db()

@app.route('/')
def index():
	return 'Hello World!'

if __name__ == '__main__':
	databaseConnect()
	app.run(host='0.0.0.0', port=8000, debug=True)
