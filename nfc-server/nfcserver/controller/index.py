from nfcserver.model.access import Access
from nfcserver.model.user import User
from flask import render_template, request, redirect, url_for
from nfcserver.db import dao
from nfcserver.blueprint import nfc
import nxppy, time


@nfc.route('/access')
def accessPage():
    users = dao.query(Access).order_by(Access.date.asc()).all()
    return render_template('index.html', users=users)


@nfc.route('/')
def indexPage():
    print('NFC TAG')
    mifare = nxppy.Mifare()

    # Print card UIDs as they are detected
    while True:
        try:
            nfcid = mifare.select()
            print(nfcid)
            user = dao.query(User).filter(User.nfcid == nfcid).first()

            new_access = Access(user.name, user.nfcid)
            dao.add(new_access)
            dao.commit()
        except nxppy.SelectError:
            # SelectError is raised if no card is in the field.
            # print('nxppy.SelectError')
            pass
        time.sleep(1)
