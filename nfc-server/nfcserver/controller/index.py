#-*- coding: utf-8 -*-
from nfcserver.model.access import Access
from nfcserver.model.user import User
from flask import render_template, request, redirect, url_for
from nfcserver.db import dao
from nfcserver.blueprint import nfc
from exception import NoneUserName
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
            if new_access == None:
                raise NoneUserName
            dao.add(new_access)
            dao.commit()
            print(new_access.name + " Access")
        except NoneUserName as e:
            print(e)
        except nxppy.SelectError:
            # SelectError is raised if no card is in the field.
            # print('nxppy.SelectError')
            print('NFC Tag를 접촉해주세요')
        time.sleep(1)
