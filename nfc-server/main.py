from nfcserver import create_app
from flask import Flask
from nfcserver.db import DBManager, dao
from flask import render_template, request, session, redirect, url_for
from nfcserver.model.user import User
import nxppy, time

app = create_app()

def nfcTag():
	print('NFC TAG')
	mifare = nxppy.Mifare()

	# Print card UIDs as they are detected
	while True:
		try:
			uid = mifare.select()
			print(uid)
			return uid
		except nxppy.SelectError:
			# SelectError is raised if no card is in the field.
			print('nxppy.SelectError')
		time.sleep(1)

@app.route('/')
def indexPage():
    return render_template('index.html')

@app.route('/', methods=['POST'])
def index():
	if request.method == 'POST':
		name = request.form['name']
#		nfcid = request.form['nfcid']
		nfcid = nfcTag()

		new_user = User(name, nfcid)

		dao.add(new_user)
		dao.commit()

		return redirect(url_for('indexPage'))

if __name__ == '__main__':
	print('start')
	print(dao)
	app.run(host='0.0.0.0', port=8000, debug=True)
