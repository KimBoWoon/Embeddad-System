from nfcserver.controller.nfctag import getNFCTag
from nfcserver.model.user import User
from flask import render_template, request, redirect, url_for
from nfcserver.db import dao
from nfcserver.blueprint import nfc


@nfc.route('/')
def indexPage():
    nfcid = getNFCTag()
    users = dao.query(User).filter(User.nfcid == nfcid).first()

    return render_template('index.html', users=users)