from nfcserver.controller.nfctag import getNFCTag
from nfcserver.model.user import User
from flask import render_template, request, redirect, url_for
from nfcserver.db import dao
from nfcserver.blueprint import nfc


@nfc.route('/signup')
def signupPage():
    return render_template('signup.html')


@nfc.route('/signup', methods=['POST'])
def signup():
    if request.method == 'POST':
        name = request.form['name']
        nfcid = getNFCTag()

        new_user = User(name, nfcid)

        dao.add(new_user)
        dao.commit()

        return redirect(url_for('.signupPage'))
